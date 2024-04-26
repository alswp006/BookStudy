import java.util.ArrayList;
import java.util.List;

public class DotComeBust {
    List<DotCom> dotComList = new ArrayList<>();
    GameHelper helper = new GameHelper();
    int numOfGuesses = 0;
    public void setUpGame(){
        //객체 생성 초기화,  이름 위치 지정, 게임방법 출력
        String[] addr = {"kim", "min", "je"};
        for (int i = 0; i<3; i++){
            dotComList.add(new DotCom());
            dotComList.get(i).setName(addr[i]);
        }
        for (DotCom dotComToSet : dotComList){
            ArrayList<String> newLocation = helper.placeDotCom();
            dotComToSet.setLocationCells(newLocation);
        }
    }
    public void startPlaying(){
        //Dotcom의 모든 객체가 없어질 떄까지 사용자로부터 추측한 위치를 받아들이고 checkUserGuess메소드 호출
        while (!dotComList.isEmpty()){
            checkUserGuess(helper.getUserInput("Enter a guess"));
        }
        finishGame();
    }
    public void checkUserGuess(String userGuess){
        //남아있는 모든 Dotcom 객체에 대해 순환문을 돌리며 각 DotCom 객체의 checkYourself메소드 호출
        numOfGuesses++;
        String result = "miss";
        for (DotCom docComToTest : dotComList){
            result = docComToTest.checkYourSelf(userGuess);
            if (result.equals("hit")){
                break;
            }
            if (result.equals("kill")){
                dotComList.remove(docComToTest);
                break;
            }
        }
        System.out.println(result);
    }
    public void finishGame(){
        //모든 객체들을 가라앉힐 때까지 추측횟수를 바탕으로 사용자 성적 출력
        System.out.println("All Doc Coms are dead");
        if (numOfGuesses <= 18){
            System.out.println(numOfGuesses+" good");
        }else{
            System.out.println(numOfGuesses + "notBad");
        }
    }
}
