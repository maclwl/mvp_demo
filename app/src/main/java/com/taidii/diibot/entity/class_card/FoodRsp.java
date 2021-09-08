package com.taidii.diibot.entity.class_card;

import java.util.List;

/**
 * Created by zhukaifeng on 2018/7/16.
 */

public class FoodRsp {


    /**
     * content : {"id":26,"recipe":18,"weekday":0,"breakfast":["鸡蛋","牛奶"],"lunch":["牛肉"],"noon":["牛肉"]}
     * success : 1
     */

    private ContentBean content;
    private int success;
    private EnergyBean energy;
    private NutritionBean nutrition;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public EnergyBean getEnergy() {
        return energy;
    }

    public void setEnergy(EnergyBean energy) {
        this.energy = energy;
    }

    public NutritionBean getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionBean nutrition) {
        this.nutrition = nutrition;
    }

    public static class ContentBean {
        /**
         * id : 26
         * recipe : 18
         * weekday : 0
         * breakfast : ["鸡蛋","牛奶"]
         * lunch : ["牛肉"]
         * noon : ["牛肉"]
         */

        private int id;
        private int recipe;
        private int weekday;
        private List<String> breakfast;
        private List<String> lunch;
        private List<String> noon;
        private List<String> morning_snack;
        private List<String> dinner;
        private String breakfast_img;
        private String breakfast_img_thumbnail;
        private String lunch_img;
        private String lunch_img_thumbnail;
        private String noon_img;
        private String noon_img_thumbnail;
        private String morning_snack_img;
        private String morning_snack_img_thumbnail;
        private String dinner_img;
        private String dinner_img_thumbnail;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRecipe() {
            return recipe;
        }

        public void setRecipe(int recipe) {
            this.recipe = recipe;
        }

        public int getWeekday() {
            return weekday;
        }

        public void setWeekday(int weekday) {
            this.weekday = weekday;
        }

        public List<String> getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(List<String> breakfast) {
            this.breakfast = breakfast;
        }

        public List<String> getLunch() {
            return lunch;
        }

        public void setLunch(List<String> lunch) {
            this.lunch = lunch;
        }

        public List<String> getNoon() {
            return noon;
        }

        public void setNoon(List<String> noon) {
            this.noon = noon;
        }

        public List<String> getMorning_snack() {
            return morning_snack;
        }

        public void setMorning_snack(List<String> morning_snack) {
            this.morning_snack = morning_snack;
        }

        public List<String> getDinner() {
            return dinner;
        }

        public void setDinner(List<String> dinner) {
            this.dinner = dinner;
        }

        public String getBreakfast_img() {
            return breakfast_img;
        }

        public void setBreakfast_img(String breakfast_img) {
            this.breakfast_img = breakfast_img;
        }

        public String getBreakfast_img_thumbnail() {
            return breakfast_img_thumbnail;
        }

        public void setBreakfast_img_thumbnail(String breakfast_img_thumbnail) {
            this.breakfast_img_thumbnail = breakfast_img_thumbnail;
        }

        public String getLunch_img() {
            return lunch_img;
        }

        public void setLunch_img(String lunch_img) {
            this.lunch_img = lunch_img;
        }

        public String getLunch_img_thumbnail() {
            return lunch_img_thumbnail;
        }

        public void setLunch_img_thumbnail(String lunch_img_thumbnail) {
            this.lunch_img_thumbnail = lunch_img_thumbnail;
        }

        public String getNoon_img() {
            return noon_img;
        }

        public void setNoon_img(String noon_img) {
            this.noon_img = noon_img;
        }

        public String getNoon_img_thumbnail() {
            return noon_img_thumbnail;
        }

        public void setNoon_img_thumbnail(String noon_img_thumbnail) {
            this.noon_img_thumbnail = noon_img_thumbnail;
        }

        public String getMorning_snack_img() {
            return morning_snack_img;
        }

        public void setMorning_snack_img(String morning_snack_img) {
            this.morning_snack_img = morning_snack_img;
        }

        public String getMorning_snack_img_thumbnail() {
            return morning_snack_img_thumbnail;
        }

        public void setMorning_snack_img_thumbnail(String morning_snack_img_thumbnail) {
            this.morning_snack_img_thumbnail = morning_snack_img_thumbnail;
        }

        public String getDinner_img() {
            return dinner_img;
        }

        public void setDinner_img(String dinner_img) {
            this.dinner_img = dinner_img;
        }

        public String getDinner_img_thumbnail() {
            return dinner_img_thumbnail;
        }

        public void setDinner_img_thumbnail(String dinner_img_thumbnail) {
            this.dinner_img_thumbnail = dinner_img_thumbnail;
        }
    }

    public static class NutritionBean {
        private double vitamins_b1;
        private double vitamins_b2;
        private double fat;
        private double carbohydrate;
        private double water;
        private double protein;
        private double niacin;
        private double vitamins_c;
        private double vitamins_a;
        private double sodium;
        private double vitamins_e;
        private double calories;
        private double calcium;
        private double dietary_fiber;
        private double iron;
        private double cholesterol;
        private double weight;

        public double getVitamins_b1() {
            return vitamins_b1;
        }

        public void setVitamins_b1(double vitamins_b1) {
            this.vitamins_b1 = vitamins_b1;
        }

        public double getVitamins_b2() {
            return vitamins_b2;
        }

        public void setVitamins_b2(double vitamins_b2) {
            this.vitamins_b2 = vitamins_b2;
        }

        public double getFat() {
            return fat;
        }

        public void setFat(double fat) {
            this.fat = fat;
        }

        public double getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(double carbohydrate) {
            this.carbohydrate = carbohydrate;
        }

        public double getWater() {
            return water;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getNiacin() {
            return niacin;
        }

        public void setNiacin(double niacin) {
            this.niacin = niacin;
        }

        public double getVitamins_c() {
            return vitamins_c;
        }

        public void setVitamins_c(double vitamins_c) {
            this.vitamins_c = vitamins_c;
        }

        public double getVitamins_a() {
            return vitamins_a;
        }

        public void setVitamins_a(double vitamins_a) {
            this.vitamins_a = vitamins_a;
        }

        public double getSodium() {
            return sodium;
        }

        public void setSodium(double sodium) {
            this.sodium = sodium;
        }

        public double getVitamins_e() {
            return vitamins_e;
        }

        public void setVitamins_e(double vitamins_e) {
            this.vitamins_e = vitamins_e;
        }

        public double getCalories() {
            return calories;
        }

        public void setCalories(double calories) {
            this.calories = calories;
        }

        public double getCalcium() {
            return calcium;
        }

        public void setCalcium(double calcium) {
            this.calcium = calcium;
        }

        public double getDietary_fiber() {
            return dietary_fiber;
        }

        public void setDietary_fiber(double dietary_fiber) {
            this.dietary_fiber = dietary_fiber;
        }

        public double getIron() {
            return iron;
        }

        public void setIron(double iron) {
            this.iron = iron;
        }

        public double getCholesterol() {
            return cholesterol;
        }

        public void setCholesterol(double cholesterol) {
            this.cholesterol = cholesterol;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static class EnergyBean {
        private float noon_per;
        private float morning_snack_per;
        private float lunch_per;
        private float noon;
        private float total_calories;
        private float dinner_per;
        private float lunch;
        private float dinner;
        private float morning_snack;
        private float breakfast_per;
        private float breakfast;

        public float getNoon_per() {
            return noon_per;
        }

        public void setNoon_per(float noon_per) {
            this.noon_per = noon_per;
        }

        public float getMorning_snack_per() {
            return morning_snack_per;
        }

        public void setMorning_snack_per(float morning_snack_per) {
            this.morning_snack_per = morning_snack_per;
        }

        public float getLunch_per() {
            return lunch_per;
        }

        public void setLunch_per(float lunch_per) {
            this.lunch_per = lunch_per;
        }

        public float getNoon() {
            return noon;
        }

        public void setNoon(float noon) {
            this.noon = noon;
        }

        public float getTotal_calories() {
            return total_calories;
        }

        public void setTotal_calories(float total_calories) {
            this.total_calories = total_calories;
        }

        public float getDinner_per() {
            return dinner_per;
        }

        public void setDinner_per(float dinner_per) {
            this.dinner_per = dinner_per;
        }

        public float getLunch() {
            return lunch;
        }

        public void setLunch(float lunch) {
            this.lunch = lunch;
        }

        public float getDinner() {
            return dinner;
        }

        public void setDinner(float dinner) {
            this.dinner = dinner;
        }

        public float getMorning_snack() {
            return morning_snack;
        }

        public void setMorning_snack(float morning_snack) {
            this.morning_snack = morning_snack;
        }

        public float getBreakfast_per() {
            return breakfast_per;
        }

        public void setBreakfast_per(float breakfast_per) {
            this.breakfast_per = breakfast_per;
        }

        public float getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(float breakfast) {
            this.breakfast = breakfast;
        }
    }
}
