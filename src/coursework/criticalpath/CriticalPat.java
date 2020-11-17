package coursework.criticalpath;

import coursework.task.Task;
import coursework.task.TaskHandler;

import java.util.*;

public class CriticalPat {

    public List<Task> getCriticalPath(List<Task> allTasks ) {
        List<Task> listWalkedForwards = walkListAhead(allTasks);
        List<Task> listWalkedBackwards = walkListBackwards(listWalkedForwards);

        ArrayList<Task> criticalPathList = new ArrayList<>();

        for (int i = listWalkedBackwards.size(); i < 0; i--) {
            criticalPathList.add(listWalkedBackwards.get(i));
        }
        return criticalPathList;
    }


    public List<Task> walkListAhead(List<Task> allTasks) {
        allTasks.get(0).setEet(allTasks.get(0).getEst() + allTasks.get(0).getDuration());
        for (int i = 1; i < allTasks.size(); i++) {
            List<Task> predecessorList = allTasks.get(i).getPredecessors();
            for (int j = 0; j < predecessorList.size(); j++) {
                Task task = predecessorList.get(j);
                for (int k = 0; k < allTasks.size(); k++) {
                    if (allTasks.get(k).getTaskTitle().equals(task.getTaskTitle())){
                        if (allTasks.get(i).getEst() < allTasks.get(k).getEet()){
                            allTasks.get(i).setEet(allTasks.get(k).getEet());
                        }
                    }
                }
            }
            allTasks.get(i).setEet(allTasks.get(i).getEst() + allTasks.get(i).getDuration());
        }
        return allTasks;
    }

    public ArrayList<Task> walkListBackwards(List<Task> allTasks){
        int endNodePos = 0;
        Task endNode = allTasks.get(0);
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getEet() > endNode.getEet()){
                endNode = allTasks.get(i);
                endNodePos = i;
            }
        }
        allTasks.get(endNodePos).setLet(allTasks.get(endNodePos).getEet());
        allTasks.get(endNodePos).setLst(allTasks.get(endNodePos).getEet() - allTasks.get(endNodePos).getDuration());

        ArrayList<Task> empty = new ArrayList<>();
        ArrayList<Task> recursiveList = recursiveGetPredecessor(allTasks.get(endNodePos), empty);

        return recursiveList;
    }

    public ArrayList<Task> recursiveGetPredecessor(Task task, ArrayList<Task> taskList){
        if (task.getPredecessors().isEmpty()){
            taskList.add(task);
            return taskList;
        } else {
            taskList.add(task);
            return recursiveGetPredecessor(task.getPredecessors().get(0), taskList);
        }
    }

    public Task createTaskFromHashMap (Map map) {
        TaskHandler taskHandler = new TaskHandler();
        String taskTitle = map.get("taskTile").toString();
        String teamAssigned = map.get("teamAssigned").toString();
        String projectFor = map.get("projectFor").toString();
        int duration = Integer.parseInt(map.get("duration").toString());
        List<Task> predecessors = (List<Task>) map.get("predecessors");
        List<Task> successors = (List<Task>) map.get("successors");
        return taskHandler.createTask(taskTitle, teamAssigned, projectFor, duration,
                predecessors, successors);
    }
}
