package yogaLMS.dao.yogaclass;

import yogaLMS.dao.GenericDaoFileImpl;
import yogaLMS.dto.log.Log;
import yogaLMS.dto.yogaclass.LogClass;
import yogaLMS.dto.yogaclass.YogaClass;

import java.io.*;
import java.util.*;

public class LogClassDaoFileImpl extends GenericDaoFileImpl<LogClass> implements LogClassDao {

    private Map<Long, LogClass> map = super.getMap();
    private final String LOG_CLASS_FILE = "logClassFile.txt";
    private final String STRING_DELIMITER = "::";

    @Override
    public void loadEntities() {
        // load LogClass information from file to Map
        Scanner scanner = null;

        try {
            // create Scanner object for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(LOG_CLASS_FILE)));
        } catch (IOException e) {
            e.printStackTrace(); // TODO : add app PersistenceException and throw on this method, handle in controller
        }
        // currentLine holds the most recent line read from the file
        String currentLine;

        // currentToken holds each of the parts of the currentLine after it has been split on our STRING_DELIMITER
        String[] currentTokens;

        // Go through each line in LOG_CLASS_FILE, decoding each line into a LogClass object
        // process while we have more lines in the file
        if(scanner != null){
            while(scanner.hasNextLine()) {
                // get the next line in the file
                currentLine = scanner.nextLine();
                //break up the line into tokens
                currentTokens = currentLine.split(STRING_DELIMITER);
                // create a new LogClass object and put it into the map LogClassMap
                LogClass currentLogClass = new LogClass();
                // set the remaining items on currentLogClass
                // file setup Long id::Long logId::Long classId
                currentLogClass.setId(Long.parseLong(currentTokens[0]));
                // lazy load class and log objects to set in service layer later
                // TODO: add logic to service layer to populate
                YogaClass yogaClass = new YogaClass();
                yogaClass.setId(Long.parseLong(currentTokens[1]));
                currentLogClass.setYogaClass(yogaClass);
                Log log = new Log();
                log.setId(Long.parseLong(currentTokens[2]));
                currentLogClass.setLog(log);
                // put currentItem into the map
                this.map.put(currentLogClass.getId(), currentLogClass);
            }
            scanner.close();
        }
    }

    @Override
    public void writeEntities() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(LOG_CLASS_FILE));
        } catch (IOException e) {
            e.printStackTrace(); // TODO throw application persistence exception
        }

        // write out the LogClass objects to the file
        List<LogClass> logClassList = this.retrieveAll();
        for(LogClass logClass : logClassList){
            // write the LogClass object to the file as a line
            // TODO refactor to avoid potential null pointer exception, this should work when implementing persistence exception and throwing in the catch clause
            out.println(logClass.getId() + STRING_DELIMITER
                    + logClass.getYogaClass().getId() + STRING_DELIMITER
                    + logClass.getLog().getId() + STRING_DELIMITER);

            // force PrinteWriter to write line to the file
            out.flush();
        }
        out.close();
    }
}
