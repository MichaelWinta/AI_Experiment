package Game5.Game5;

public class KI4 {
    public static void main(String[] args) {
        System.out.println("hi");
        char[][] map;
        int maxX = 6;
        int maxY = 6;

        // fill map with nothing
        map = new char[maxX][maxY];
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                map[i][j] = '*';
            }
        }

        map[0][0] = 'X';
        map[0][1] = 'X';
        map[0][2] = 'X';

        map[0][3] = 'X';
        // map[5][2] = 'X';

        // map[3][4] = 'X';
        // map[2][2] = 'O';
        //map[2][3] = 'O';
        // map[3][2] = 'O';
        // map[5][0] = 'X';

        // double score = KI2.scoreMove(map, 3, 2, 'O', 'X', true, 5, -2, 2);
        // System.out.println(score);
        // System.out.println("--------------");

        String move = KI4.move(map, 'X', 'O');
        System.out.println(move);
    }
    private static int getConnectedCoins(char[][] map, int posX, int posY, int dirX, int dirY) {
        int numberConnected = 0;
        char player = map[posY][posX];

        posX = posX + dirX;
        posY = posY + dirY;

        while (0 <= posY && posY < map.length && 0 <= posX && posX < map[0].length && player == map[posY][posX]) {
            posX = posX + dirX;
            posY = posY + dirY;
            numberConnected++;
        }
        return numberConnected;
    }

    private static boolean hasWon(char[][] map, int posX, int posY) {
        int top = KI4.getConnectedCoins(map, posX, posY, 0, -1);
        int bottom = KI4.getConnectedCoins(map, posX, posY, 0, 1);
        int vertical = top + bottom + 1;

        int left = KI4.getConnectedCoins(map, posX, posY, -1, 0);
        int right = KI4.getConnectedCoins(map, posX, posY, 1, 0);
        int horizontal = left + right + 1;

        int leftTop = KI4.getConnectedCoins(map, posX, posY, -1, -1);
        int rightBottom = KI4.getConnectedCoins(map, posX, posY, 1, 1);
        int diagonalBottom = leftTop + rightBottom + 1;

        int leftBottom = KI4.getConnectedCoins(map, posX, posY, -1, 1);
        int rightTop = KI4.getConnectedCoins(map, posX, posY, 1, -1);
        int diagonalTop = leftBottom + rightTop + 1;

        return vertical >= 5 || horizontal >= 5 || diagonalBottom >= 5 || diagonalTop >= 5;
    }

    // alpha = me
    // beta = opponent
    private static double scoreMove(char[][] map, int posX, int posY, char meToken, char opponentToken, boolean isPlayer, int maxDepth, double alpha, double beta) {
        double score = 0;

        if (isPlayer) {
            map[posY][posX] = meToken;
            if (KI4.hasWon(map, posX, posY)) {
                score = 1;
            } else if (maxDepth > 0) {
                double worstScore = 2;
                boolean hasMoved = false;
                boolean run = true;

                for (int y = 0; y < map.length && run; y++) {
                    for (int x = 0; x < map[0].length && run; x++) {
                        if (map[y][x] == '*') {
                            double newScore = KI4.scoreMove(map, x, y, meToken, opponentToken, false, maxDepth-1, alpha, beta);
                            worstScore = Math.min(newScore, worstScore);
                            hasMoved = true;

                            if (worstScore <= alpha) {
                                // System.out.println("Skipped alpha: " + String.valueOf(maxDepth));
                                run = false;
                            }
                            if (worstScore < beta) {
                                beta = worstScore;
                            }
                        }
                    }
                }
                if (hasMoved) {
                    score = worstScore;
                }
            }
        } else {
            map[posY][posX] = opponentToken;
            if (KI4.hasWon(map, posX, posY)) {
                score = -1;
            } else if (maxDepth > 0) {
                double bestScore = -2;
                boolean hasMoved = false;
                boolean run = true;

                for (int y = 0; y < map.length && run; y++) {
                    for (int x = 0; x < map[0].length && run; x++) {
                        if (map[y][x] == '*') {
                            double newScore = KI4.scoreMove(map, x, y, meToken, opponentToken, true, maxDepth-1, alpha, beta);
                            bestScore = Math.max(newScore, bestScore);
                            hasMoved = true;

                            if (bestScore >= beta) {
                                // System.out.println("Skipped beta: " + String.valueOf(maxDepth));
                                run = false;
                            }
                            if (bestScore > alpha) {
                                alpha = bestScore;
                            }
                        }
                    }
                }
                if (hasMoved) {
                    score = bestScore;
                }
            }
        }

        map[posY][posX] = '*';
        return score;
    }

    private static double getDistance(int x, int y) {
        return Math.abs(x - 2.5) + Math.abs(y - 2.5);
    }

    public static String move(final char[][] map, char meToken, char opponentToken) {
        int moveX = -1;
        int moveY = -1;
        double bestScore = -2.0;
        double bestDistance = 6;

        char[][] newMap = new char[map.length][map[0].length];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                newMap[y][x] = map[y][x];
            }
        }


        double beta = 2.0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '*') {
                    double newScore = KI4.scoreMove(newMap, x, y, meToken, opponentToken, true, 9, bestScore, beta);
                    // System.out.println(String.valueOf(x) + "x" + String.valueOf(y) + ": " + String.valueOf(newScore));
                    double newDistance = KI4.getDistance(x, y);
                    if (newScore > bestScore || (newScore == bestScore && newDistance < bestDistance && false)) {
                        bestScore = newScore;
                        moveX = x;
                        moveY = y;
                        bestDistance = newDistance;
                    }
                }
            }
        }

        System.out.println("v1 - " + String.valueOf(moveX) + "x" + String.valueOf(moveY) + ": " + String.valueOf(bestScore));

        return String.valueOf(moveX) + "x" + String.valueOf(moveY);
    
    }

}
