package project12.src;

public class Rook extends Piece {
    public Rook(PieceColor color,Position position){
        super(color, position);
    }
    @Override
    public boolean isValidMove(Position newPosition,Piece[][] board){
        //Rook can move vertically or horizontally any number of squares.
        //they cannot jump over pieces.
        if(position.getRow()==newPosition.getRow()){
            int columnStart=Math.min(position.getColumn(),newPosition.getColumn())+1;
            int columnEnd=Math.max(position.getColumn(),newPosition.getColumn());
            for(int column=columnStart;column <columnEnd;column++)
            {
                if (board [position.getRow()][column]!=null){
                    return false;
                    //there's a piece in the way
                }
            }
            

        }
        
        else if(position.getColumn()==newPosition.getColumn()){
            int rowStart=Math.min(position.getRow(),newPosition.getRow())+1;
            int rowEnd= Math.max(position.getRow(),newPosition.getRow());
            for(int row =rowStart;row<rowEnd;row++)
            {
                if(board[row][position.getColumn()]!=null)
                {
                    return false;//there's a piece in the way
                }
            }
        }
                else{
                    return false;
                    //not a valid rook move (not straight line)
                }
            
        
        Piece destinationPiece = board[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true; // move to empty square
        } else return destinationPiece.getColor() != this.getColor(); // cannot move to a square occupied by own piece
        // can capture opponent's piece
        
}
}
