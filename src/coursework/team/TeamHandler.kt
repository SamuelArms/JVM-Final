package coursework.team

import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

class TeamHandler {

    private val teams = ArrayList<Team>()

    // Create a team object
    fun createTeam(teamTitle: String, members: List<String>) : Team{
        return Team(teamTitle, members)
    }

    fun createTeamFromTransferFile(): Team {
        // define a empty string
        var teamLine = ""
        // using lambda expressions get the line from the data transfer file
        File("src/coursework/data transfer.json").useLines { lines -> lines.forEach { line -> teamLine = line} }
        // create the json object with the line
        val jsonObj = JSONObject(teamLine)
        // get the needed values from the json object
        var projectTitle = jsonObj.get("teamTitle").toString()
        var tasksAssigned = jsonObj.getJSONArray("members").toList()
        // create a team with the values
        // create a team with the values
        return createTeam(projectTitle, tasksAssigned as List<String>)
    }

    fun getTeamsFromSave(): ArrayList<Team>{
        // retrieve the data from the save file using lambda expressions
        File("src/coursework/persistence/team Persistence.txt").useLines { lines ->
            lines.forEach { line ->
            // within the lambda expression retrieve the data and get the needed values to create the team
            val jsonObj = JSONObject(line)
            var projectTitle = jsonObj.get("teamTitle").toString()
            var tasksAssigned = jsonObj.getJSONArray("members").toList()
            // create the team and add it to the teams list within the lambda expression
            teams.add(createTeam(projectTitle, tasksAssigned as List<String>))
        }}
        // return a list of teams
        return teams
    }
    fun getSaveString(team: Team): String {
        // turn the team into a json object
        var jsonObj = JSONObject(team)
        // turn the json json object into string
        return jsonObj.toString()
    }

    fun getDisplay(team: Team): String{
        // add the team title to a string
        var display = "Team Title:\n\t ${team.teamTitle}\nMembers:\n\t"
        for (member in team.members){
            // for every member within the team add it to the string
            display += "$member \n\t"
        }
        return display
    }

}