package project12.src;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class ChessGameGUI extends JFrame {
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final ChessGame game = new ChessGame();
    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
        {
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
            put(Pawn.class, "\u265F");
        }
    };

    public ChessGameGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        addGameResetOption();
        pack();
        setVisible(true);
    }

    private void initializeBoard() {

        for (int row = 0; row < squares.length; row++) {
            System.out.println("Initializing row: " + row);
            System.out.println("squares.length: " + squares.length);
            for (int col = 0; col < squares[row].length; col++) {

                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                squares[row][col] = square;

            }

        }
        refreshBoard();
    }

    private void refreshBoard() {
        ChessBoard board = game.getBoard();
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares.length; col++) {
                if (squares[row][col] == null) {
                    continue; // Skip if the square is not initialized
                }
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    // if using unicode symbols
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = piece.getColor() == PieceColor.WHITE ? Color.PINK : Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);
                } else {
                    squares[row][col].clearPieceSymbol();
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        System.out.println("clicked on square:"+row+","+col);
        boolean moveMade = game.handleSquareSelection(row, col);
        System.out.println("moveMade: "+moveMade);
        clearHighlights();
        if (moveMade) {
            System.out.println("Refreshing board after move");
            refreshBoard();
            checkGameState();
            checkGameOver();
            
            // If single player mode, make AI move after a short delay
            if (game.isSinglePlayerMode()) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(1000); // 1 second delay for better UX
                        boolean aiMoveMade = game.makeAIMove();
                        if (aiMoveMade) {
                            System.out.println("AI move made");
                            refreshBoard();
                            checkGameState();
                            checkGameOver();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } else if (game.isPieceSelected()) {
            highlightLegalMoves(new Position(row, col));
        }

        refreshBoard();
    }

    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        boolean inCheck = game.isInCheck(currentPlayer);
        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }

    }

    private void highlightLegalMoves(Position position) {
        List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
        for (Position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setBackground(Color.GREEN);
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(new Color(205, 133, 63)); // brown
                } else {
                    squares[row][col].setBackground(new Color(255, 228, 196)); // light color (bisque)
                }
            }
        }
    }

    private void addGameResetOption() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void resetGame() {
        game.resetGame();
        refreshBoard();
    }

    private void checkGameOver() {
        if (game.isCheckmate(game.getCurrentPlayerColor())) {

            int response = JOptionPane.showConfirmDialog(this, "Checkmate! Would you like to play again?", "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {

        
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}
