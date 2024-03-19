package main;

public enum GameStates {
	 
	PLAYING, MENU, REPLAY, EDIT, GAME_OVER;
	
	public static GameStates gameState = MENU;
	
	public static void SetGameState(GameStates state) {
		gameState = state;
	}
	

}
