package project12.src;
public class Knight extends Piece {
    public Knight(PieceColor color, Position position )
    {
        super(color,position);
    }
    @Override
    public boolean isValidMove(Position newPosition,Piece[][] board)
    {
        if(newPosition.equals(this.position))
        {
            return false;
            //cannot move to the same position 
        }
        int rowDiff=Math.abs(this.position.getRow()-newPosition.getRow());
        int colDiff=Math.abs(this.position.getColumn()-newPosition.getColumn());
        //check for the 'l'shape move pattern 
        boolean isValidMove =(rowDiff==2&&colDiff==1)||(rowDiff==1&&colDiff==2);
        if(!isValidMove)
        {
            return false ;
            //not a valid  knight move
        }
        //Move is valid if the destination on square is empty or contains an opponent's
        //piece
        Piece targetPiece=board[newPosition.getRow()][newPosition.getColumn()];
        if (targetPiece==null)
        {
            return true;
            //the square is empty,move is valid
        }
        else{
            return targetPiece.getColor()!=this.getColor();
            //can capture if it's an opponent's piece 
        }
    }
 
}
