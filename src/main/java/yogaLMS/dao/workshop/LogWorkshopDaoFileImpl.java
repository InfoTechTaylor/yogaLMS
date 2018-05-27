package yogaLMS.dao.workshop;

import yogaLMS.dao.GenericDaoFileImpl;
import yogaLMS.dao.YogaLMSPersistenceException;
import yogaLMS.dto.log.Log;
import yogaLMS.dto.workshop.LogWorkshop;
import yogaLMS.dto.workshop.Workshop;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class LogWorkshopDaoFileImpl extends GenericDaoFileImpl<LogWorkshop> implements LogWorkshopDao {


    private Map<Long, LogWorkshop> logWorkshopMap = super.getMap();
    private String filename;
    private final String STRING_DELIMITER = "::";

    public LogWorkshopDaoFileImpl(String filename) {
        this.filename = filename;
    }

    @Override
    public void loadEntities() throws YogaLMSPersistenceException {
        // load information from file to Map
        Scanner scanner = null;
        try {
            // create Scanner object for reading the file
            scanner = new Scanner(new BufferedReader(new FileReader(filename)));
            // currentLine holds the most recent line read from the file
            String currentLine;

            // currentToken holds each of the parts of the currentLine after it has been split on our STRING_DELIMITER
            String[] currentTokens;

            // Go through each line in filename, decoding each line into an object
            // process while we have more lines in the file
            if(scanner != null){
                while(scanner.hasNextLine()) {
                    // get the next line in the file
                    currentLine = scanner.nextLine();
                    //break up the line into tokens
                    currentTokens = currentLine.split(STRING_DELIMITER);
                    // create a new object and put it into the Map
                    LogWorkshop currentLogWorkshop = new LogWorkshop();
                    // set the remaining items on currentLogWorkshop
                    // file setup id::logId::workshopId
                    currentLogWorkshop.setId(Long.parseLong(currentTokens[0]));

                    // lazy load log and workshop objects
                    Log log = new Log();
                    log.setId(Long.parseLong(currentTokens[1]));
                    currentLogWorkshop.setLog(log);
                    Workshop workshop = new Workshop();
                    workshop.setId(Long.parseLong(currentTokens[2]));
                    currentLogWorkshop.setWorkshop(workshop);

                    // put currentLogWorkshop into the map
                    logWorkshopMap.put(currentLogWorkshop.getId(), currentLogWorkshop);
                }
                scanner.close();
            }

        } catch (IOException e) {
            throw new YogaLMSPersistenceException("Error reading from file.");
        }
    }

    @Override
    public void writeEntities() throws YogaLMSPersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            // write out the LogWorkshop objects to the file
            List<LogWorkshop> logWorkshops = this.retrieveAll();
            for(LogWorkshop currentLogWorkshop : logWorkshops){
                // write the object to the file as a line
                out.println(currentLogWorkshop.getId() + STRING_DELIMITER
                        + currentLogWorkshop.getLog().getId() + STRING_DELIMITER
                        + currentLogWorkshop.getWorkshop().getId());
                out.flush(); // force PrinteWriter to write line to the file
            }
            out.close();
        } catch (IOException e) {
            throw new YogaLMSPersistenceException("Error writing to file.");
        }
    }
}
