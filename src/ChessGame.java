package project12.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessGame {
  private ChessBoard board ;
  private boolean whiteTurn=true;
  private boolean isSinglePlayerMode = true;
  private Random random = new Random();
  
  public ChessGame()
  {
    this.board=new ChessBoard();
  }


public ChessBoard getBoard(){
return this.board;
}
public void resetGame(){
    this.board =new ChessBoard();
    this.whiteTurn=true;

}
public PieceColor getCurrentPlayerColor(){
    return whiteTurn?PieceColor.WHITE:PieceColor.BLACK;
}
private Position selectedPosition;
public boolean isPieceSelected(){
    return selectedPosition!=null;
}
public boolean handleSquareSelection(int row,int col){
    Piece clickedPiece = board.getPiece(row, col);
    if (selectedPosition == null) {
        // Select the piece if it belongs to the current player
        if (clickedPiece != null && clickedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            selectedPosition = new Position(row, col);
        }
        return false;
    } else {
        // Try to move the selected piece to the clicked square
        boolean moveMade = makeMove(selectedPosition, new Position(row, col));
        selectedPosition = null;
        return moveMade;
    }
}
public boolean makeMove(Position start,Position end){
    if (start == null || end == null) {
        // Invalid move: start or end position is not set
        return false;
    }
    Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
    if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
        return false;
    }
    if (movingPiece.isValidMove(end, board.getBoard())) {
        board.movePiece(start, end);
        whiteTurn = !whiteTurn;
        return true;
    }
    return false;
}
public boolean isInCheck(PieceColor kingColor) {
    Position kingPosition = findKingPosition(kingColor);
    for (int row = 0; row < board.getBoard().length; row++) {
        for (int col = 0; col < board.getBoard()[row].length; col++) {
            Piece piece = board.getPiece(row, col);
            if (piece != null && piece.getColor() != kingColor) {
                if (piece.isValidMove(kingPosition, board.getBoard())) {
                    return true;
                }
            }
        }
    }
    return false;
}
private Position findKingPosition(PieceColor color){
for(int row=0;row<board.getBoard().length;row++)
{


for(int col=0;col<board.getBoard()[row].length;col++)
{
    Piece piece=board.getPiece(row,col);
    if(piece instanceof King &&piece.getColor()==color)
    {
        return new Position(row,col);
    } 
  }
 }
 throw new RuntimeException("king not found,which should never happen.");
}
public boolean isCheckmate(PieceColor kingColor)
{
    if(! isInCheck(kingColor))
    {
        return false;
    }
    Position kingPosition=findKingPosition(kingColor);
    King king =(King) board.getPiece(kingPosition.getRow(),kingPosition.getColumn());
    for(int rowOffset=-1;rowOffset<=1;rowOffset++){
        for (int colOffset = -1; colOffset < 1; colOffset++){
        if(rowOffset==0&&colOffset==0)
        {
            continue;
        }
        Position newPosition=new Position(kingPosition.getRow()+rowOffset, kingPosition.getColumn()+ colOffset);
        if(isPositionOnBoard(newPosition)&&king.isValidMove(newPosition,board.getBoard())&&!wouldBeInCheckAfterMove( kingColor,kingPosition,newPosition)){
            return false;
        }
        }
    }
    return true;
}
private boolean isPositionOnBoard(Position position)
{
    return position.getColumn()>=0&&position.getColumn()>=0&&position.getColumn()>=0&& position.getColumn()<board.getBoard()[0].length;
}
private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
    Piece temp= board.getPiece(to.getRow(), to.getColumn());
    board.setPiece(to.getRow(),to.getColumn(),board.getPiece(from.getRow(),from.getColumn()));
    board.setPiece(from.getRow(),from.getColumn(),null);
    boolean inCheck=isInCheck(kingColor);
    board.setPiece(from.getRow(),from.getColumn(),board.getPiece(to.getRow(),to.getColumn()));
    board.setPiece(to.getRow(),to.getColumn(),temp);
    return inCheck;
}


public List<Position> getLegalMovesForPieceAt(Position position) {
    Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn()); 
    if(selectedPiece == null) {
        return new ArrayList<>();
    }
    List<Position> legalMoves = new ArrayList<>();
    switch(selectedPiece.getClass().getSimpleName()) {
        case "Pawn" -> addPawnMoves(position, selectedPiece.getColor(), legalMoves);
        case "Rook" -> addLineMoves(position, new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}, legalMoves);
        case "Knight" -> addSingleMoves(position, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}}, legalMoves);
        case "Bishop" -> addLineMoves(position, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}, legalMoves);
        case "Queen" -> addLineMoves(position,new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}, legalMoves);
        case "King" -> addSingleMoves(position,new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}, legalMoves);
    }
    return legalMoves;

}


private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {

 for (int[] d: directions){
        Position newPos = new Position(position.getRow()+d[0], position.getColumn()+d[1]);
    while(isPositionOnBoard(newPos)) {
        if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
            legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
            newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
        } else {
            if(board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor()) {
                legalMoves.add(newPos);
            }
            break;
            
        } 
    } 
 } 
}
    

private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
    for (int[] move : moves) {
        Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
        if (isPositionOnBoard(newPos)&&(board.getPiece(newPos.getRow(), newPos.getColumn()) == null || 
            board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board.getPiece(position.getRow(), position.getColumn()).getColor())) {
            legalMoves.add(newPos);
        }
    } 
}




private void addPawnMoves(Position position,PieceColor color,List<Position>legalMoves){
    int direction=color==PieceColor.WHITE?-1:1;
    Position newPos=new Position(position.getRow()+direction, position.getColumn());
    if(isPositionOnBoard(newPos)&& board.getPiece (newPos.getRow(),newPos.getColumn())==null){
        legalMoves.add(newPos);
    }
    if((color==PieceColor.WHITE&& position.getRow()==6)||(color==PieceColor.BLACK&& position.getRow()==1)){
        newPos=new Position(position.getRow()+2*direction,position.getColumn());
        Position intermediatePos=new Position(position.getRow()+direction,position.getColumn());
        if(isPositionOnBoard(newPos)&& board.getPiece(newPos.getRow(),newPos.getColumn())==null && board.getPiece(intermediatePos.getRow(),intermediatePos.getColumn())==null)
        {
            legalMoves.add(newPos);
        }
    }
    int[] captureCols={
        position.getColumn()+1,position.getColumn()-1};
        for( int col:captureCols)
        {
            newPos=new Position(position.getRow()+direction,position.getColumn()+col);
            if(isPositionOnBoard(newPos)&&board.getPiece(newPos.getRow(),newPos.getColumn())!=null&& board.getPiece(newPos.getRow(),newPos.getColumn()).getColor()!=color)
            {
                legalMoves.add(newPos);
            }
        }

}

// AI Method for automatic opponent moves
public boolean makeAIMove() {
    if (!isSinglePlayerMode || whiteTurn) {
        return false;
    }
    
    // Find all black pieces and their legal moves
    List<Position> allBlackPieces = new ArrayList<>();
    List<List<Position>> allLegalMoves = new ArrayList<>();
    
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            Piece piece = board.getPiece(row, col);
            if (piece != null && piece.getColor() == PieceColor.BLACK) {
                List<Position> legalMoves = getLegalMovesForPieceAt(new Position(row, col));
                if (!legalMoves.isEmpty()) {
                    allBlackPieces.add(new Position(row, col));
                    allLegalMoves.add(legalMoves);
                }
            }
        }
    }
    
    if (allBlackPieces.isEmpty()) {
        return false;
    }
    
    // Randomly select a piece and a move
    int randomPieceIndex = random.nextInt(allBlackPieces.size());
    Position fromPos = allBlackPieces.get(randomPieceIndex);
    List<Position> moves = allLegalMoves.get(randomPieceIndex);
    Position toPos = moves.get(random.nextInt(moves.size()));
    
    // Make the move
    return makeMove(fromPos, toPos);
}

public boolean isSinglePlayerMode() {
    return isSinglePlayerMode;
}

}


