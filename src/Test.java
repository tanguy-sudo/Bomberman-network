import controller.ControllerBombermanGame;
public class Test {

    /**
     * Fonction qui lance l'application
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
        controllerBombermanGame.lunchGame("/home/etud/Documents/s8/reseau/Bomberman-network/layouts/niveau3.lay", "1", true);
    }

}