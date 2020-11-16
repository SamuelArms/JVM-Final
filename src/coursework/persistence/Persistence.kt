package coursework.persistence

abstract class Persistence {

    companion object{
        // companion object to ensure the file persistence is implemented
        fun createFilePersistence() = FilePersistence()
    }

    // define the needed functions to save all the details
    abstract fun saveProject(saveString: String)
    abstract fun saveTeam(saveString: String)
    abstract fun saveTask(saveString: String)
}