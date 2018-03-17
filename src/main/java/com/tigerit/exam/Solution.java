package com.tigerit.exam;


import static com.tigerit.exam.IO.*;
import java.util.*;
import java.io.*;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point

        // this line has used for only testing purpose
        //initFileIO();

        // reading the number of test cases
        int numOfTestCase = readLineAsInteger();

        for(int test=1; test<=numOfTestCase; test++){
            // reading the number of tables for current test case
            int numOfTables = readLineAsInteger();

            // initializing table map
            tableMap = new HashMap<String, Integer>(numOfTables+1, 1);

            // initializing ArrayList for column name map
            colMap = new ArrayList<Map<String, Integer>>();

            // initializing ArrayList for column serial as their order
            nameOfColList = new ArrayList<ArrayList<String>>();

            // initializing database
            dataBase = new ArrayList<ArrayList<ArrayList<Integer>>>();

            // creating table
            for(int table=0; table<numOfTables; table++){
                buildTable(table);
            }

            // printing test case number
            String testStr = "Test: " + Integer.toString(test);
            printLine(testStr);

            // reading the number of quries for current test case
            int numOfQuery = readLineAsInteger();

            // processing query one by one
            for(int query=0; query<numOfQuery; query++){
                queryProcessor();
            }


            // clearing all the containers
            dataBase.clear();
            tableMap.clear();
            colMap.clear();
        }

        // this line has used for only testing purpose
        //stopFileIO();
    }

    // mapping the table names
    public static Map<String, Integer> tableMap;

    // mapping the table columns
    public static ArrayList<Map<String, Integer>> colMap;

    // name of columns in serial
    public static ArrayList<ArrayList<String>> nameOfColList;

    // database of tables
    public static ArrayList<ArrayList<ArrayList<Integer>>> dataBase;

    // output table
    public static ArrayList<ArrayList<Integer>> outputTable;

    // ArrayList of pair of key and row no
    public static ArrayList<ArrayList<Integer>> pairList;

    // Constant for Not Found
    public static final int NOT_FOUND = -1;


    /*
     * Test Block Start

    // temporary file for check only testing purpose
    public static BufferedWriter writer;

    public static void initFileIO(){
        try {
            writer = new BufferedWriter(new FileWriter("/home/ashif/Output.txt"));
        } catch (IOException ex) {
            // inside the catch block of initFileIO
        }
    }

    public static void stopFileIO(){
        try {
            writer.close();
        } catch (IOException ex) {
             // inside the catch block of stopFileIO
        }
    }

    public static void printLine(Object value){
        try {
            writer.write(String.valueOf(value));
            writer.newLine();
        } catch (Exception ex) {
            // inside the catch block of printLine
        }
    }
    /*
     * Test Block End
     */


    /**
      * integerParser is a method that takes a String as an input
      * and parse it as an array of integer and return the array
      */
    public static int[] integerParser(String lineString){
        // splitting the input String using space
        String[] items = lineString.split(" ");

        int[] results = new int[items.length];

        // parsing the Strings as integer
        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException ex) {
                results = null;
            };
        }

        return results;
    }



    /*
     * Method for adding table to Database
     */
    public static void buildTable(int tableId){
        // reading table name
        String tableName = readLine();
        tableMap.put(tableName, tableId);

        // reading and parsing number of row and column
        String str = readLine();
        int tmp[] = integerParser(str);
        int col = tmp[0], row = tmp[1];

        // initializing column map
        colMap.add(new HashMap<String, Integer>(col+1, 1));

        // initializing name of columns array list
        nameOfColList.add(new ArrayList<String>());

        // reading and splitting the name of columns
        String[] colName = readLine().split(" ");

        // mapping the name of columns
        for(int colId=0; colId<col; colId++){
            colMap.get(tableId).put(colName[colId], colId);
            nameOfColList.get(tableId).add(colName[colId]);
        }


        // data entry starts here
        // adding new table to database
        dataBase.add(new ArrayList<ArrayList<Integer>>());

        // reading and saving the data of table
        for(int curRow=0; curRow<row; curRow++){
            // adding new record to current table
            dataBase.get(tableId).add(new ArrayList<Integer>());

            // reading and parsing current row
            String tupleString = readLine();
            int tuple[] = integerParser(tupleString);

            // pushing data into current row
            for(int curCol=0; curCol<col; curCol++){
                dataBase.get(tableId).get(curRow).add(tuple[curCol]);
            }
        }


    }


    /*
     * Dump output 1.0
     * Only for testing purpose
     */
    public static void dumpOutputGenerator(int tableId){
        if(dataBase.get(tableId).isEmpty()) return;

        for(int row = 0; row<dataBase.get(tableId).size(); row++){
            outputTable.add(new ArrayList<Integer>());

            for(int col = 0; col<dataBase.get(tableId).get(row).size(); col++){
                outputTable.get(row).add(dataBase.get(tableId).get(row).get(col));
            }
        }

    }



    /*
     * Method for Query processing
     */
    public static void queryProcessor(){
        // initializing output table
        outputTable = new ArrayList<ArrayList<Integer>>();

        // initializing table map
        Map<String, Integer> tableLocalMap = new HashMap<String, Integer>(5, 1);

        // array list for column's name to be printed
        ArrayList<String> colNameList = new ArrayList<String>();

        // array list for column's id
        ArrayList<Integer> colIdList = new ArrayList<Integer>();

        // list of table id for the column's in serial
        ArrayList<Integer> colTableList = new ArrayList<Integer>();



        // Reading input query
        // reading and foramtting select clause
        String selectClause = readLine().replaceAll(",", " ").replaceAll(" +", " ").trim().substring(6).trim();

        // reading and formatting from clause
        String fromClause = readLine().replace(" +", " ").trim().substring(4).trim();

        // reading and formatting join clause
        String joinClause = readLine().replace(" +", " ").trim().substring(4).trim();

        // reading and foramtting select clause
        String onClause = readLine().replaceAll("=", " ").replaceAll(" +", " ").trim().substring(2).trim();

        // important
        // reading blank line
        readLine();

        // processing from clause
        String fromItems[] = fromClause.split(" ");
        tableLocalMap.put(fromItems[0], tableMap.get(fromItems[0]));
        if(fromItems.length>1){
            tableLocalMap.put(fromItems[1], tableMap.get(fromItems[0]));
        }


        // processing join clause
        String joinItems[] = joinClause.split(" ");
        tableLocalMap.put(joinItems[0], tableMap.get(joinItems[0]));
        if(joinItems.length>1){
            tableLocalMap.put(joinItems[1], tableMap.get(joinItems[0]));
        }


        // processing select clause
        // when select *
        if(selectClause.charAt(0)=='*'){
            // adding all column from first table
            int firstTable = tableMap.get(fromItems[0]);
            int colFirst = nameOfColList.get(firstTable).size();

            for(int col = 0; col<colFirst; col++){
                colTableList.add(firstTable);
                colIdList.add(col);
                colNameList.add(nameOfColList.get(firstTable).get(col));
            }

            // adding all column from second table
            int secondTable = tableMap.get(joinItems[0]);
            int colSecond = nameOfColList.get(secondTable).size();

            for(int col = 0; col<colSecond; col++){
                colTableList.add(secondTable);
                colIdList.add(col);
                colNameList.add(nameOfColList.get(secondTable).get(col));
            }
        }//specified column and table
        else{
            // processing select clause
            String selectItems[] = selectClause.split(" ");

            for(int i=0; i<selectItems.length; i++){
                String items[] = selectItems[i].replace("."," ").split(" ");

                /*printLine(selectItems[0]);
                printLine(items.length);*/

                int tableId = tableLocalMap.get(items[0]);
                int colId = colMap.get(tableId).get(items[1]);

                colTableList.add(tableId);
                colIdList.add(colId);
                colNameList.add(nameOfColList.get(tableId).get(colId));
            }
        }

        /// For Test purpose
        ///dumpOutputGenerator(tableMap.get(tableName));

        // printing the name of columns in order
        String result = String.join(" ", colNameList);
        printLine(result);


        // join Processing starts here
        String onItems[] = onClause.split(" ");

        // first table and column
        String onFirst[] = onItems[0].replace("."," ").split(" ");
        int firstTableId = tableLocalMap.get(onFirst[0]);
        int firstColId = colMap.get(firstTableId).get(onFirst[1]);

        // second table and column
        String onSecond[] = onItems[1].replace("."," ").split(" ");
        int secondTableId = tableLocalMap.get(onSecond[0]);
        int secondColId = colMap.get(secondTableId).get(onSecond[1]);


        // initializing pairList
        pairList = new ArrayList<ArrayList<Integer>>();

        if(!dataBase.get(secondTableId).isEmpty()){
            for(int row = 0; row<dataBase.get(secondTableId).size(); row++){
                pairList.add(new ArrayList<Integer>());
                pairList.get(row).add(dataBase.get(secondTableId).get(row).get(secondColId));
                pairList.get(row).add(row);
            }
        }

        if(!pairList.isEmpty()){
            // sorting pair list
            sortPairList();

            // tracker of output list current index
            int outIdx = 0;

            for(int rowFirst = 0; rowFirst<dataBase.get(firstTableId).size(); rowFirst++){
                int firstElement = dataBase.get(firstTableId).get(rowFirst).get(firstColId);

                // searcing in the pair list
                int low = firstAppearance(firstElement);
                int high = lastAppearance(firstElement);

                if(low!=NOT_FOUND && high!=NOT_FOUND){
                    // iterating through low to high in the pair list
                    for(int rowSecond = low; rowSecond<=high; rowSecond++){
                        // mapping table to row
                        Map<Integer, Integer> rowMap = new HashMap<Integer, Integer>(3, 1);
                        rowMap.put(firstTableId, rowFirst);
                        rowMap.put(secondTableId, pairList.get(rowSecond).get(1));

                        // initializing new row to output list
                        outputTable.add(new ArrayList<Integer>());

                        for(int curIdx = 0; curIdx<colTableList.size(); curIdx++){
                            int table = colTableList.get(curIdx);
                            int row = rowMap.get(table);
                            int col = colIdList.get(curIdx);

                            outputTable.get(outIdx).add(dataBase.get(table).get(row).get(col));
                        }

                        // increasing outIdx
                        outIdx++;
                    }
                }
            }
        }


        // clearing pair list
        pairList.clear();

        // printing output table
        outputPrinter();

        // printing blank line
        printLine("");

    }


    /*
     * This method find the first appearance of
     * specified key in the pairList
     */
    public static int firstAppearance(int key){
        int first = 0, last = pairList.size() - 1, ret = NOT_FOUND;

        while(first<=last){
            int mid = (first+last)/2;

            if(pairList.get(mid).get(0)==key){
                if(ret==NOT_FOUND) ret = mid;
                else ret = Math.min(mid, ret);
            }

            if(pairList.get(mid).get(0)<key){
                first = mid + 1;
            }
            else{
                last = mid - 1;
            }
        }

        return ret;
    }



    /*
     * This method find the last appearance of
     * specified key in the pairList
     */
    public static int lastAppearance(int key){
        int first = 0, last = pairList.size() - 1, ret = NOT_FOUND;

        while(first<=last){
            int mid = (first+last)/2;

            if(pairList.get(mid).get(0)==key){
                if(ret==NOT_FOUND) ret = mid;
                else ret = Math.max(mid, ret);
            }

            if(pairList.get(mid).get(0)>key){
                last = mid - 1;
            }
            else{
                first = mid + 1;
            }
        }

        return ret;
    }



    /*
     * Method to sort the ArrayList represnting pair list
     */
    public static void sortPairList(){
        Collections.sort(pairList , new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> firstList, ArrayList<Integer> secondList) {
                    return firstList.get(0).compareTo(secondList.get(0));
                }
        });
    }

    /*
     * Method for printing outputTable
     */
    public static void outputPrinter(){
        if(outputTable.isEmpty()){
            outputTable.clear();
            return;
        }

        // sorting the output table
        customComparatorOutput();

        // printing the output table
        for(int curRow=0; curRow<outputTable.size(); curRow++){
            ArrayList<String> outList = new ArrayList<>();

            for(int curCol=0; curCol<outputTable.get(curRow).size(); curCol++){
                outList.add(outputTable.get(curRow).get(curCol).toString());
            }

            // join with a space to concat all strings.
            String result = String.join(" ", outList);
            printLine(result);
        }

        // clearing output container
        outputTable.clear();
    }


    /*
     * Custom Method For Sorting 2d ArrayList of Integer
     */
     public static void customComparatorOutput(){
         Collections.sort(outputTable , new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> firstList, ArrayList<Integer> secondList) {
                    int sizeOfList = firstList.size();
                    int curIdx = 0;

                    while(curIdx<sizeOfList){
                        if(curIdx==(sizeOfList-1)) break;

                        if(firstList.get(curIdx)!=secondList.get(curIdx)){
                            return firstList.get(curIdx).compareTo(secondList.get(curIdx));
                        }
                        else curIdx++;
                    }

                    return firstList.get(curIdx).compareTo(secondList.get(curIdx));
                }
        });
     }


}
