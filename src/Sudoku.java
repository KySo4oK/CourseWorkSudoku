import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Sudoku {

    public Cell[][] matrix = new Cell[9][9];

    private int quantityOfConstCells = 81;
    private int quantityOfRowsAndColumsWithoutConst = 18;
    private int expectQuantityOfConstCells = 35;
    private int expectQuantityOfRowsAndColumsWithoutConst = 18;
    private ArrayList<Cell> openedCells;
    private ArrayList<Cell> constCells;
    private PriorityQ openCellsPQ = new PriorityQ();
    private String fileName = "save.txt";
    public boolean emptyFile = false;
    private ArrayList<Cell> changedConst;

    Sudoku(){
        newMatrix();
    }

    Sudoku(String fileName) throws IOException {
        this.fileName = fileName;
        loadFromFile();
    }

    private void loadFromFile(){
        try(FileReader reader = new FileReader(fileName)){
            Scanner scan = new Scanner(reader);
            ArrayList<String> lines = new ArrayList<>();
            while (scan.hasNextLine()){
                lines.add(scan.nextLine());
            }
            for(int i=0; i < 9; i++){
                String[] line = lines.get(i).split(",");
                for(int j = 0; j < 9; j++){
                    matrix[i][j] = new Cell(Integer.parseInt(line[j]));
                }
            }
            for(int i=0; i < 9; i++){
                String[] line = lines.get(i + 9).split(",");
                for(int j = 0; j < 9; j++){
                    if(Integer.parseInt(line[j]) == 0) {
                        matrix[i][j].setConst(false);
                    } else if(Integer.parseInt(line[j]) == 1){
                        matrix[i][j].setConst(true);
                    } else System.out.println("Errror" + line[j]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            emptyFile = true;
            System.out.println("Error");
        } catch (IOException e) {
            e.printStackTrace();
            emptyFile = true;
            System.out.println("Error");
        }
    }

    public void setToFile(){
        try(FileWriter fileWriter = new FileWriter(fileName)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                   fileWriter.write(matrix[i][j].getValue() + ",");
                }
                fileWriter.write("\r\n");
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if(matrix[i][j].getConst()){
                        fileWriter.write(1 + ",");
                    } else {
                        fileWriter.write(0 + ",");
                    }
                }
                fileWriter.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialConstCells(){
        constCells = new ArrayList<>();
        changedConst = new ArrayList<>();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                matrix[i][j].setConst(true);
                constCells.add(matrix[i][j]);
            }
        }
    }

    private void returnConst(){
        for(int i = 0 ; i < changedConst.size(); i++){
            if(changedConst.get(i).getConst()){
                changedConst.get(i).setConst(false);
                openedCells.add(changedConst.get(i));
            } else {
                changedConst.get(i).setConst(true);

            }
        }
        changedConst.clear();
    }

    private void rebuildPQ() {
        openCellsPQ = new PriorityQ();
        for (int i = 0; i < openedCells.size(); i++){
            Cell current = openedCells.get(i);
            openCellsPQ.push(new PriorityQNode(current, searchPossibleValues(
                    findIndexX(current),
                    findIndexY(current))));
        }
    }

    private int findIndexX(Cell cell){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(matrix[i][j] == cell){
                    return i;
                }
            }
        }
        return -1;
    }

    private int findIndexY(Cell cell){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(matrix[i][j] == cell){
                    return j;
                }
            }
        }
        return -1;
    }

    private void deleteConst(){
        while (canDelete() && !constCells.isEmpty()) {
            Cell temp = constCells.get((int)(Math.random()*81) % constCells.size());
            constCells.remove(temp);
            int i = findIndexX(temp);
            int j = findIndexY(temp);
            if(isOneSolution(i,j)){
                quantityOfConstCells --;
                checkQuantityOfRowsAndColums(i,j);
            }
        }
        if(expectQuantityOfConstCells < quantityOfConstCells){
            newMatrix();
        }
    }

    private boolean isOneSolution(int k, int z){
        Cell current = matrix[k][z];
        current.setConst(false);
        openedCells.add(current);
        changedConst.add(current);
        rebuildPQ();
        while(!openCellsPQ.isEmpty()){
            PriorityQNode temp = openCellsPQ.pop();
            if(temp.getPriority() != 1){
                openedCells.remove(current);
                returnConst();
                current.setConst(true);
                return false;
            } else {
                temp.getCoreCell().setConst(true);
                openedCells.remove(temp.getCoreCell());
                rebuildPQ();
                changedConst.add(temp.getCoreCell());
            }
        }
        returnConst();
        return true;
    }

    private void newMatrix(){
        basicMatrix();
        mix();
        initialConstCells();
        openedCells = new ArrayList<>();
        quantityOfConstCells = 81;
        quantityOfRowsAndColumsWithoutConst = 18;
        deleteConst();
    }

    private void checkQuantityOfRowsAndColums(int k, int z){
        for(int i = 0; i < matrix.length; i++){
            if(matrix[k][i].getConst()){
                break;
            } else if(i==matrix.length-1){
                quantityOfRowsAndColumsWithoutConst--;
            }
        }
        for(int i = 0; i < matrix.length; i++){
            if(matrix[i][z].getConst()){
                break;
            } else if(i==matrix.length-1){
                quantityOfRowsAndColumsWithoutConst--;
            }
        }

    }

    private int searchPossibleValues(int k, int z){
        int quantity = 0;
        for(int value = 1; value <= 9; value++){
            if(!inSameBlock(k,z,value) &&
                    !inSameColum(k,z,value) &&
                    !inSameRow(k,z,value)){
                quantity++;
            }
        }
        return quantity;
    }

    private boolean inSameColum(int k, int z, int number){
        for(int i = 0; i < matrix.length; i++){
            if(matrix[i][z].getConst() && matrix[i][z].getValue() == number &&
            k != i){
                return true;
            }
        }
        return false;
    }

    private boolean inSameRow(int k, int z, int number){
        for(int i = 0; i < matrix.length; i++){
            if(matrix[k][i].getConst() && matrix[k][i].getValue() == number &&
            i != z){
                return true;
            }
        }
        return false;
    }

    private boolean inSameBlock(int k, int z, int number){
        int indexOfBlockY  = z/3;
        int indexOfBlockX = k/3;
        for(int i = indexOfBlockX*3; i < indexOfBlockX*3 + 3; i++){
            for(int j = indexOfBlockY*3; j < indexOfBlockY*3 + 3; j++){
                if(matrix[i][j].getConst() &&
                        matrix[i][j].getValue() == number &&
                        i!=k && j!=z){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDelete() {
        if (expectQuantityOfConstCells < quantityOfConstCells &&
                expectQuantityOfRowsAndColumsWithoutConst <= quantityOfRowsAndColumsWithoutConst ){
            return true;
        }
        return false;
    }

    private void mix(){
        for(int i = 0; i < 20; i++){
            transposing();
           int [] temp1 = createRandomNumbersForSmallSwap();
            swapRowsSmall(temp1[0], temp1[1]);
            temp1 = createRandomNumbersForSmallSwap();
            swapColumsSmall(temp1[0],temp1[1]);
            temp1 = createRandomNumbersForAreaSwap();
            swapColumsArea(temp1[0],temp1[1]);
            temp1 = createRandomNumbersForAreaSwap();
            swapRowsArea(temp1[0],temp1[1]);
        }
    }

    private int[] createRandomNumbersForAreaSwap(){
        int temp1 = (int)(Math.random()*3);
        int temp2 = (int)(Math.random()*3);
        while (temp1!=temp2){
            temp2 = (int)(Math.random()*3);
        }
        int[] temp = {temp1,temp2};
        return temp;
    }

    private int[] createRandomNumbersForSmallSwap(){
        int temp1 = (int)(Math.random()*9);
        int temp2;
        if(temp1%3 == 0){
            temp2 = temp1 + (int)(Math.random()*2);
        } else if((temp1 - 1)%3 == 0){
            if(Math.random() == 0) {
                temp2 = temp1 + 1;
            } else {
                temp2 = temp1 - 1;
            }
        } else {
            temp2 = temp1 - (int)(Math.random()*2);
        }
        int[] temp = {temp1,temp2};
        return temp;
    }

    private void basicMatrix(){
        for(int i = 0; i < matrix.length; i++ ){
            for(int j = 0; j < matrix.length; j++){
                matrix[i][j] = new Cell();
                matrix[i][j].setValue((j + i*3 + i/3)%9 + 1);
            }
        }
    }

    public void printMatrix(){
        for(int i = 0; i < matrix.length; i++ ){
            for(int j = 0; j < matrix.length; j++){
                System.out.print(matrix[i][j].getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void transposing() {
        for(int i = 0; i < matrix.length; i++ ){
            for(int j = 0; j < matrix.length; j++){
                int temp = matrix[j][i].getValue();
                matrix[j][i].setValue(matrix[i][j].getValue());
                matrix[i][j].setValue(temp);
            }
        }
    }

    private void swapRowsSmall(int k, int z){
        for(int i = 0; i < matrix.length; i++){
            int temp = matrix[i][z].getValue();
            matrix[i][z].setValue(matrix[i][k].getValue());
            matrix[i][k].setValue(temp);
        }
    }

    private void swapColumsSmall(int k, int z){
        for(int i = 0; i < matrix.length; i++){
            int temp = matrix[z][i].getValue();
            matrix[z][i].setValue(matrix[k][i].getValue());
            matrix[k][i].setValue(temp);
        }
    }

    private void swapRowsArea(int k, int z){
        for(int j = 0; j < 3; j++) {
            for (int i = 0; i < matrix.length; i++) {
                int temp = matrix[i][z*3 + j].getValue();
                matrix[i][z*3 + j].setValue(matrix[i][k*3 + j].getValue());
                matrix[i][k*3 + j].setValue(temp);
            }
        }
    }

    private void swapColumsArea(int k, int z){
        for(int j = 0; j < 3; j++) {
            for (int i = 0; i < matrix.length; i++) {
                int temp = matrix[z*3 + j][i].getValue();
                matrix[z*3 + j][i].setValue(matrix[k*3 + j][i].getValue());
                matrix[k*3 + j][i].setValue(temp);
            }
        }
    }
}
