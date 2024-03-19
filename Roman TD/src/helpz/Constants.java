package helpz;

public class Constants {
	
	public static class Projecttiles {
		public static final int BULLET = 0;
		public static final int CHAINS = 1;
		public static final int BULLETTUR = 2;
		
		
		public static float GetSpeed(int type) {
			switch (type) {
			case BULLET:
				return  8f;
			case BULLETTUR:
				return 4f;
			case CHAINS:
				return 6f;
			}
			return 0f;
		}
	}

	public static class Towers {
		public static final int TURRET = 0;
		public static final int TROOPER = 1;
		public static final int MAGE = 2;
		
		public static int GetTowerCost(int towerType) {
			switch (towerType) {
			case TURRET:
				return 75;
			case TROOPER:
				return 35;
			case MAGE:
				return 60;

			}
			return 0;
		}

		public static String GetName(int towerType) {
			switch (towerType) {
			case TURRET:
				return "Турель";
			case TROOPER:
				return "Штурмовик";
			case MAGE:
				return "Маг";

			}
			return "";
		}
	
		public static int GetStartDmg(int towerType) {
			switch (towerType) {
			case TURRET:
				return 15;
			case TROOPER:
				return 5;
			case MAGE:
				return 0;

			}
			return 0;
		}
		
		public static float GetDefaultRange(int towerType) {
			switch (towerType) {
			case TURRET:
				return 70;
			case TROOPER:
				return 80;
			case MAGE:
				return 70;

			}
			return 0;
			
		}
		
		public static float GetDefaultCooldown(int towerType) {
			switch (towerType) {
			case TURRET:
				return 120;
			case TROOPER:
				return 25;
			case MAGE:
				return 40;

			}
			return 0;
		}
	}

	public static class Direction {

		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;

	}

	public static class Enemies {

		public static final int STALKER = 0;
		public static final int GLADOS = 1;
		public static final int PBODY = 2;
		public static final int ATLASDOG = 3;
		
		public static int GetReward(int enemyType) {
			switch (enemyType) {
			case STALKER:
				return 15;
			case GLADOS:
				return 20;
			case PBODY:
				return 30;
			case ATLASDOG:
				return 15;
			}

			return 0;
		}

		public static float GetSpeed(int enemyType) {
			switch (enemyType) {
			case STALKER:
				return 0.5f;
			case GLADOS:
				return 0.65f;
			case PBODY:
				return 0.3f;
			case ATLASDOG:
				return 0.75f;
			}

			return 0;

		}

		public static int GetStartHealth(int enemyType) {
			switch (enemyType) {
			case STALKER:
				return 100;
			case GLADOS:
				return 60;
			case PBODY:
				return 250;
			case ATLASDOG:
				return 85;
			}
			return 0;
		}

	}

	public static class Tiles {
		public static final int WATER_TILE = 0;
		public static final int PLATE_TILE = 1;
		public static final int ROAD_TILE = 2;
		public static final int FLOOR_TILE = 3;
		public static final int TOWER_POINT = 4;

		

	}

}
