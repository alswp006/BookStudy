import java.util.ArrayList;

public class DotCom {
    private String name;
    private ArrayList<String> newLocation;

    public void setName(String name){
        this.name = name;
    }

    public void setLocationCells(ArrayList<String> newLocation) {
        this.newLocation = newLocation;
    }

    public String checkYourSelf(String userGuess) {
        String status = "miss";
        int index = newLocation.indexOf(userGuess);
        if (index>=0){
            newLocation.remove(index);
            if (newLocation.isEmpty()){
                status = "kill";
                System.out.println("you sunk" + name);
            }else{
                status = "hit";
            }
        }
        return status;
    }
}
