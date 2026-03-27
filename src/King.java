package project12.src;

public class King extends Piece{
    public King(PieceColor color,Position position)
    {
        super(color,position);
    }
    @Override
    public boolean isValidMove(Position newPosition,Piece[][] board)
    {
        int rowDiff=Math.abs(position.getRow()-newPosition.getRow());
        int colDiff=Math.abs(position.getColumn()-newPosition.getColumn());
        //king can move one square in any direction 
        boolean isOneSquareMove=rowDiff<=1&&colDiff<=1&&!(rowDiff==0 &&colDiff==0) ;
        
        if(!isOneSquareMove){
            return false; 
            //move is not within one square.
        }
        Piece destinationPiece=board[newPosition.getRow()][newPosition.getColumn()];
        return destinationPiece==null||destinationPiece.getColor()!=this.getColor();


    }

}
