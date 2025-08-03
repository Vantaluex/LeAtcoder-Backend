package com.JCC.LeAtcoderAPI.Model.ServiceObjects;

public record DifficultyObject(int beginner, int easy, int medium, int hard, int expert) {

    // Constants for score ranges
    public static final int BEGINNER_MIN = 0;
    public static final int BEGINNER_MAX = 200;
    public static final int EASY_MIN = 201;
    public static final int EASY_MAX = 300;
    public static final int MEDIUM_MIN = 301;
    public static final int MEDIUM_MAX = 450;
    public static final int HARD_MIN = 451;
    public static final int HARD_MAX = 650;
    public static final int EXPERT_MIN = 651;
    public static final int EXPERT_MAX = 1500;

    // Static method to get score range for a difficulty
    public static int[] getRange(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "beginner" -> new int[]{BEGINNER_MIN, BEGINNER_MAX};
            case "easy" -> new int[]{EASY_MIN, EASY_MAX};
            case "medium" -> new int[]{MEDIUM_MIN, MEDIUM_MAX};
            case "hard" -> new int[]{HARD_MIN, HARD_MAX};
            case "expert" -> new int[]{EXPERT_MIN, EXPERT_MAX};
            default -> new int[]{BEGINNER_MIN, EXPERT_MAX}; // "all"
        };
    }
}
