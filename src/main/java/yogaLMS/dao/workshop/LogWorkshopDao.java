package yogaLMS.dao.workshop;

import yogaLMS.dao.YogaLMSPersistenceException;
import yogaLMS.dto.workshop.LogWorkshop;

import java.util.List;

public interface LogWorkshopDao {

    public LogWorkshop create(LogWorkshop logWorkshop) throws YogaLMSPersistenceException;

    public LogWorkshop read(Long id) throws YogaLMSPersistenceException;

    public void update(LogWorkshop logWorkshop) throws YogaLMSPersistenceException;

    public void delete(LogWorkshop logWorkshop) throws YogaLMSPersistenceException;

    public List<LogWorkshop> retrieveAll() throws YogaLMSPersistenceException;
}
