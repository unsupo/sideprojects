import java.math.BigInteger;
import java.util.*;

public class SudokuBits {
    public static void main(String[] args) throws Exception {
        //give an array of ints representing the sudoku puzzle to solve where any number not in [1-9] is a blank spot
        int[] board = new int[]{
                0, -1, -1,  0, -1, -1, -1,  1,  2,
                0, -1, -1, -1, -1, -1, -1, -1,  3,
                0, -1,  2,  3, -1, -1,  4, -1, -1,
                0, -1,  1,  8, -1, -1, -1, -1,  5,
                0,  6, -1, -1,  7, -1,  8, -1, -1,
                0, -1, -1,  0, -1,  9, -1, -1,  0,
                0, -1,  8,  5, -1, -1, -1, -1, -1,
                9, -1, -1, -1,  4, -1,  5, -1, -1,
                4,  7, -1, -1, -1,  6, -1,  0, -1
        };
        /*
        839|465|712|
        146|782|953|
        752|391|486|
        ------------
        391|824|675|
        564|173|829|
        287|659|341|
        ------------
        628|537|194|
        913|248|567|
        475|916|238|
        ------------

        346|485|712|
        149|722|653|
        752|391|489|
        ------------
        231|864|975|
        564|273|831|
        887|159|244|
        ------------
        618|522|347|
        923|748|566|
        475|936|128|
        ------------
         */
        SudokuBits sb = new SudokuBits(board);
        long s = System.nanoTime();
        System.out.println(solve(sb));
        long e = System.nanoTime();
        System.out.println((e-s));

        Sudoku su = new Sudoku(board);
        s = System.nanoTime();
        System.out.println(Sudoku.solve(su));
        e = System.nanoTime();
        System.out.println((e-s));

        s = System.nanoTime();
        System.out.println(Sudoku.solve(su));
        e = System.nanoTime();
        System.out.println((e-s));

        s = System.nanoTime();
        System.out.println(solve(sb));
        e = System.nanoTime();
        System.out.println((e-s));



        /*
        s = System.nanoTime();
        System.out.println(solve(sb));
        e = System.nanoTime();
        System.out.println("diff"+(e-s));
//
        s = System.nanoTime();
        System.out.println(solve(sb));
        e = System.nanoTime();
        System.out.println("diff"+(e-s));
        s = System.nanoTime();
        System.out.println(diffSolve(sb));
        e = System.nanoTime();
        System.out.println((e-s));
        */
//        System.out.println(sb.isValidOld(1,1));

//        System.out.println(print(sb));
//        System.out.println(print(solve(sb)));
//        char[] s = BitHelper.fill(sb.orAll().toString(2),size).toCharArray();
//        String r = "";
//        for (int i = 0; i < s.length; i++) {
//            r+=s[i];
//            if((i+1)%lsize==0)
//                r+="\n";
//        }
//        System.out.println(r);
//        System.out.println(sb.isValid(1,13));
//        System.out.println(print(sb));
//        int[] ints = getRandom(40);
//        System.out.println();


    }

    public static void printIntArray(int[] a){
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+" ");
            if((i+1)%lsize==0)
                System.out.println();
        }
    }

    public static int[] getRandom(int count){
        Random r = new Random();
        SudokuBits bits = new SudokuBits();
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 9; i++) integers.add(i+1);
        int[] values = new int[size];
        while (count > 0){
            int pos = r.nextInt(size);
            Collections.shuffle(integers);
            int i = 0;
            Integer num = integers.get(i++);
            boolean isValid = true;
            while (!bits.isValid(num,pos))
                if(i>=integers.size()) {
                    isValid = false;
                    break;
                }else num = integers.get(i++);
            if(isValid) {
                bits.putNumber(num, pos);
                values[pos] = num;
                count--;
            }
        }
        return values;
    }

    private static BigInteger
            horizontal   = new BigInteger("111111111000000000000000000000000000000000000000000000000000000000000000000000000",2),
            vertical     = new BigInteger("100000000100000000100000000100000000100000000100000000100000000100000000100000000",2),
            square       = new BigInteger("111000000111000000111000000000000000000000000000000000000000000000000000000000000",2);
    private static String position = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static int lsize = 9, size = lsize*lsize;

    private int[] startingNumbers = new int[size];
    private BigInteger[] numbers = new BigInteger[lsize];

    public SudokuBits(){
        for (int i = 0; i < this.numbers.length; i++) {
            this.numbers[i]=BigInteger.ZERO;
        }
    }

    public SudokuBits(BigInteger[] numbers){
        this.numbers = numbers;
    }

    public SudokuBits(int[] startingNumbers, BigInteger[] numbers) {
        this.startingNumbers = startingNumbers;
        this.numbers = numbers;
    }

    public SudokuBits(int[] startingNumbers){
        if(startingNumbers.length != size) throw new IllegalArgumentException("Invalid length");
        this.startingNumbers = startingNumbers;
        String[] bits = new String[numbers.length];
        for (int i = 0; i < bits.length; i++)
            bits[i]="";
        for (int i = 0; i < startingNumbers.length; i++)
            for (int j = 0; j < bits.length; j++)
                if((j+1)==startingNumbers[i])
                    bits[j]+="1";
                else
                    bits[j]+="0";
        for (int i = 0; i < bits.length; i++)
            numbers[i] = new BigInteger(bits[i],2);
    }

    private boolean checker(BigInteger a, BigInteger b, BigInteger checker, int size, int osize){
        BigInteger s = checker;
        for (int i = 0; i < osize; i++){
            //if a is in this check (row/column/square)
            if(a.and(s).equals(a)) { //no need for a second loop because this is the test row
                //and if b has a number in this row then invalid item
                if (!b.and(s).equals(BigInteger.ZERO))
                    return false;
                return true;
            }
            s = s.shiftRight(size);
        }
        return true;
    }
    private BigInteger getNumAtPos(int pos){
        char[] c = position.toCharArray();
        c[pos]='1';
        return new BigInteger(String.valueOf(c),2);
    }

    private int[] convertToIntArray() {
        int[] board = new int[size];
        char[][] s = new char[size][lsize];
        for (int i = 0; i < numbers.length; i++)
            s[i]=BitHelper.fill(numbers[i].toString(2),size).toCharArray();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < 9; j++)
                if(s[j][i]=='1')
                    board[i]=j+1;
        return board;
    }

    public boolean isValidOld(int num, int pos){
        int[] board = convertToIntArray();
        //check if something is already there
        if(board[pos] != 0) return false;
        int rowStart = pos/lsize, rowEnd = rowStart+lsize,
            colStart = pos%lsize, squareStart = (rowStart/3)*3*9,
            squareEnd = squareStart+9*2+3;
        //check horizontal
        for (int i = rowStart; i < rowEnd; i++)
            if(board[i]==num)
                return false;
        //check vertical
        for (int i = colStart; i < lsize; i+=lsize)
            if(board[i]==num)
                return false;
        //check square
        int j=1;
        for (int i = squareStart; i < squareEnd; i++) {
            if(board[i]==num)
                return false;
            if((i+1)%3==0)
                i = squareStart + 9*(j++);
            if(board[i]==num)
                return false;
        }
        return true;
    }
    public boolean isValid(int num, int pos){
        BigInteger numpos = numbers[num-1];
        BigInteger number = getNumAtPos(pos);
        for (int i = 0; i < numbers.length; i++)
            if(!numbers[i].equals(BigInteger.ZERO) && number.and(numbers[i]).equals(numbers[i]))
                return false;
        //check horizontal
        if(!checker(number,numpos,horizontal,lsize,lsize))
            return false;
        //check vertical
        if(!checker(number,numpos,vertical,1,lsize))
            return false;
        //check square
        if(!checker(number,numpos,square,3,3))
            return false;
        if(!checker(number,numpos,square.shiftRight(9*3),3,3))
            return false;
        if(!checker(number,numpos,square.shiftRight(9*6),3,3))
            return false;
        return true;
    }

    private BigInteger orAll() {
        BigInteger b = numbers[0];
        for (int i = 1; i < numbers.length; i++)
            b = b.or(numbers[i]);
        return b;
    }

    @Override
    public SudokuBits clone(){
        return new SudokuBits(startingNumbers,numbers.clone());
    }

    @Override
    public String toString() {
        String[] b = new String[this.numbers.length];
        for (int i = 0; i < this.numbers.length; i++)
            b[i]=BitHelper.fill(this.numbers[i].toString(2),size);
        String f = "";
        for (int j = 0; j < b[0].toCharArray().length; j++) {
            boolean isSomething = false;
            for (int i = 0; i < b.length; i++)
                if (b[i].toCharArray()[j] == '1'){
                    f += "" + (i + 1);
                    isSomething = true;
                }
            if(!isSomething) f+="-";
            if((j+1)%3==0) f+="|";
            if((j+1)%lsize==0) {
                f += "\n";
                if((j+1)%(3*9)==0) {
                    for (int i = 0; i < lsize + 3; i++)
                        f += "-";
                    f += "\n";
                }
            }
        }
        return f;
    }

    //driver
    public static List<Pair<Integer, List<Integer>>> getAllowedOptions(SudokuBits sb){
        //get all places without a number
        char[] c = BitHelper.fill(sb.orAll().toString(2),size).toCharArray();
        // get all allowed moves
        List<Pair<Integer,List<Integer>>> optionCount = new ArrayList<>();
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '0') continue;
            else {
                Pair<Integer, List<Integer>> p = new Pair<>(i, new ArrayList<>());
                for (int j = 1; j <= 9; j++)
                    if (sb.isValid(j, i))
                        p.end.add(j);
                optionCount.add(p);
            }
        }
        Collections.sort(optionCount, (a,b)->new Integer(a.end.size()).compareTo(b.end.size()));
        return optionCount;
    }
    public static List<Pair<Integer, List<Integer>>> getAllowedOptionsSubset(SudokuBits sb, List<Pair<Integer, List<Integer>>> subset){
        // get all allowed moves
        List<Pair<Integer, List<Integer>>> subsetClone = new ArrayList<>();
        for (Pair<Integer, List<Integer>> p : subset) {
            List<Integer> allowed = new ArrayList<>();
            for (Integer i : p.end)
                if (sb.isValid(i, p.start))
                    allowed.add(i);
            Pair<Integer, List<Integer>> pp = new Pair<>(p.start, allowed);
            subsetClone.add(pp);
        }
        Collections.sort(subsetClone, (a,b)->new Integer(a.end.size()).compareTo(b.end.size()));
        return subsetClone;
    }
    public static SudokuBits diffSolve(SudokuBits sb){
        return _diffSolve(sb,getAllowedOptions(sb));
    }

    private static SudokuBits _diffSolve(SudokuBits sb, List<Pair<Integer, List<Integer>>> optionCount) {
        for(Pair<Integer, List<Integer>> s : optionCount){
            for(Integer i : s.end) {
                SudokuBits clone = sb.clone();
                if(clone.isValid(i,s.start))
                    clone.putNumber(i , s.start);
                ArrayList<Pair<Integer, List<Integer>>> temp = new ArrayList<>(optionCount);
                temp.remove(s);
                List<Pair<Integer, List<Integer>>> options = getAllowedOptionsSubset(clone,temp);//getAllowedOptionsSubset(clone,temp);//getAllowedOptions(clone);
                List<Pair<Integer, List<Integer>>> remove = new ArrayList<>();
                boolean notAllowed = false;
                for(Pair<Integer, List<Integer>> t : options){
                    if(t.end.size()==0)
                        notAllowed = true;
                    if(t.end.size() > 1 || t.end.size() == 0)
                        break;
                    if(clone.isValid(t.end.get(0), t.start))
                        clone.putNumber(t.end.get(0), t.start);
                    remove.add(t);
                }
                if(notAllowed)break;
                options.removeAll(remove);
                SudokuBits ss = _diffSolve(clone, options);
                if(ss.orAll().bitCount()==size)
                    return ss;
            }
        }
        return sb;
    }

    public static SudokuBits solve(SudokuBits sb){
        //get all places without a number
        char[] c = BitHelper.fill(sb.orAll().toString(2),size).toCharArray();
        int i;
        for (i = 0; i < c.length; i++)
            if(c[i]=='0') break;
        return _solve(sb, c, i);
    }private static SudokuBits _solve(SudokuBits sb, char[] c, int i){
        if(i>=size)
            return sb;
        List<Integer> nums = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        Collections.shuffle(nums);
        for (Integer j : nums) {
            if (sb.isValid(j, i)) {
                SudokuBits clone = sb.clone();
                clone.putNumber(j, i);
                char[] cc = BitHelper.fill(clone.orAll().toString(2),size).toCharArray();
                int k;
                for (k = i + 1; k < c.length; k++)
                    if(cc[k]=='0') break;
                if(k>=size)
                    return clone;
                SudokuBits s = _solve(clone, cc, k);
                if(s.orAll().bitCount()==size)
                    return s;
            }
        }
        return sb;
    }

    public void putNumber(int num, int pos) {
        numbers[num-1]=numbers[num-1].or(getNumAtPos(pos));
    }

    public static void print(SudokuBits sb){
        System.out.println(sb);
    }

    private static void getAllPositions(){
        int r = (int) Math.sqrt(lsize);
        String h = "", v = "", s = "";
        for (int i = 0; i < size; i++) {
            //horizontal
            if(i<lsize) h+="1";
            else h+="0";
            //vertical
            if(i==0 || i%lsize==0) v+="1";
            else v+="0";
            //square
            if(i<r || (i>=lsize && i<lsize+r) || (i>=lsize*2 && i<lsize*2+r)) s+="1";
            else s+="0";
        }
        String f = "new BigInteger(\"%s\",2);";
        System.out.println(String.format(f,h));
        System.out.println(String.format(f,v));
        System.out.println(String.format(f,s));
    }
}
