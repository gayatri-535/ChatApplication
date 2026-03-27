package project12.src;

public class Queen extends Piece
{
    public Queen(PieceColor color,Position position)
    {
        super(color,position);
    }
    @Override
    public boolean isValidMove(Position newPosition,Piece[][] board)
    {
      if(newPosition.equals(this.position)){
        return false;
      }  
        int rowDiff=Math.abs(position.getRow()-newPosition.getRow());
        int colDiff=Math.abs(position.getColumn()-newPosition.getColumn());
        boolean straightLine =this.position.getRow()== newPosition.getRow()||this.position.getColumn()==newPosition.getColumn();
        boolean diagonal=rowDiff==colDiff;
        if(!straightLine&&!diagonal){
            return false;

        }
        int rowDirection =Integer.compare(newPosition.getRow(),this.position.getRow());
        int colDirection =Integer.compare(newPosition.getColumn(),this.position.getColumn());
        int currentRow= this.position.getRow()+rowDirection;
        int currentCol= this.position.getColumn()+colDirection;
        while(currentRow!=newPosition.getRow()||currentCol!=newPosition.getColumn());
        {
            if(board[currentRow][currentCol]!=null)
            {
                return false;
                //path is blocked
            }
            currentRow+=rowDirection;
            currentCol+=colDirection;
        }
        //the move is valid if the destination is empty or contains an opponent's piece
        Piece destinationPiece=board[newPosition.getRow()][newPosition.getColumn()
        ];
        return destinationPiece==null||destinationPiece.getColor()!=this.getColor();

    }
}
