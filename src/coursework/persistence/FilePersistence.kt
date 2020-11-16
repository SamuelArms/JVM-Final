package coursework.persistence

import java.io.File

class FilePersistence : Persistence(){

    override fun saveProject(saveString: String) {
        // Write the projects to a text file for persistence
        File("src/coursework/persistence/project Persistence.txt").writeText(saveString)
    }

    override fun saveTeam(saveString: String) {
        // Write the teams to a text file for persistence
        File("src/coursework/persistence/team Persistence.txt").writeText(saveString)
    }
    override fun saveTask(saveString: String) {
        // Write the tasks to a text file for persistence
        File("src/coursework/persistence/task Persistence.txt").writeText(saveString)
    }

}