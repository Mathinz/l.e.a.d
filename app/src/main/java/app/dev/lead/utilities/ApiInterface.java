package app.dev.lead.utilities;


import app.dev.lead.models.pojos.DocAppointmentPojo;
import app.dev.lead.models.pojos.LoginPojo;
import app.dev.lead.models.pojos.BasicPojo;
import app.dev.lead.models.pojos.MedAlertPojo;
import app.dev.lead.models.pojos.StepsPojo;
import app.dev.lead.models.pojos.VitalSignPojo;
import app.dev.lead.models.pojos.WaterPojo;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    //    @Headers({"x-api-key: CODEX@123"})
    @FormUrlEncoded
    @POST("register.php")
    Call<BasicPojo> makeAccount(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("number") String number,
            @Field("age") String age,
            @Field("race") String race,
            @Field("gender") String gender,
            @Field("address") String address);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginPojo> makeLogin(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("insert_medication_alert.php")
    Call<BasicPojo> addMedicationAlert(
            @Field("user_id") String user_id,
            @Field("medicine_name") String medicineName,
            @Field("medicine_reason") String medicineReason,
            @Field("often") String often,
            @Field("other") String other);

    @FormUrlEncoded
    @POST("insert_doctor_appointment.php")
    Call<BasicPojo> addDoctorAppointment(
            @Field("user_id") String user_id,
            @Field("doctor_name") String doctor_name,
            @Field("appointment_location") String appointmentLocation,
            @Field("appointment_reason") String appointmentReason,
            @Field("date") String date,
            @Field("time") String time);


    @FormUrlEncoded
    @POST("update_medication_alert.php")
    Call<BasicPojo> updateMedicationAlert(
            @Field("medicine_id") String medicine_id,
            @Field("medicine_name") String medicineName,
            @Field("medicine_reason") String medicineReason,
            @Field("often") String often,
            @Field("other") String other);

    @FormUrlEncoded
    @POST("update_doctor_appointment.php")
    Call<BasicPojo> updateDoctorAppointment(
            @Field("appointment_id") String appointment_id,
            @Field("doctor_name") String doctor_name,
            @Field("appointment_location") String appointmentLocation,
            @Field("appointment_reason") String appointmentReason,
            @Field("date") String date,
            @Field("time") String time);


    @FormUrlEncoded
    @POST("delete_medication_alert.php")
    Call<BasicPojo> deleteMedicationAlert(@Field("medicine_id") String medicine_id);

    @FormUrlEncoded
    @POST("delete_doctor_appointment.php")
    Call<BasicPojo> deleteDoctorAppointment(@Field("appointment_id") String appointment_id);

    @FormUrlEncoded
    @POST("fetch_medication_alert.php")
    Call<MedAlertPojo> getMedicationAlert(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fetch_doctor_appointment.php")
    Call<DocAppointmentPojo> getDoctorAppointment(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("insert_steps.php")
    Call<BasicPojo> addStepsData(
            @Field("user_id") String user_id,
            @Field("total_steps") int total_steps,
            @Field("steps_date") String steps_date,
            @Field("steps_time") String steps_time,
            @Field("steps_distance") String steps_distance,
            @Field("steps_calories") String steps_calories);

    @FormUrlEncoded
    @POST("insert_water.php")
    Call<BasicPojo> addWaterData(
            @Field("user_id") String user_id,
            @Field("water_date") String water_date,
            @Field("total_water") String total_water);

    @FormUrlEncoded
    @POST("fetch_steps.php")
    Call<StepsPojo> getStepsData(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fetch_water.php")
    Call<WaterPojo> getWaterData(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("insert_vital_sign.php")
    Call<BasicPojo> addVitalSignData(
            @Field("user_id") String user_id,
            @Field("date") String date,
            @Field("glucose_level") String glucose_level,
            @Field("blood_sugar") String blood_pressure,
            @Field("body_temperature") String body_temperature);

    @FormUrlEncoded
    @POST("fetch_vital_sign.php")
    Call<VitalSignPojo> getVitalSignData(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("insert_meal_survey.php")
    Call<BasicPojo> addMealSurvey(@Field("user_id") String userId,
                                  @Field("prefix") String prefix,
                                  @Field("day_of_week") String dayOfWeek,
                                  @Field("meal") String meal,
                                  @Field("foods") String foods,
                                  @Field("food_quantity") String foodQuantity,
                                  @Field("other") String other,
                                  @Field("other_quantity") String otherQuantity,
                                  @Field("snack") String snack,
                                  @Field("snack_quantity") String snackQuantity);

    @FormUrlEncoded
    @POST("insert_program_evaluation_survey.php")
    Call<BasicPojo> addProgramEvaluationSurvey(@Field("user_id") String userId,
                                  @Field("prefix") String prefix,
                                  @Field("q1") String q1,
                                  @Field("q2") String q2,
                                  @Field("q3") String q3,
                                  @Field("q4") String q4,
                                  @Field("q5") String q5,
                                  @Field("q6") String q6,
                                  @Field("q7") String q7,
                                  @Field("q8") String q8,
                                  @Field("q9") String q9);

    @FormUrlEncoded
    @POST("insert_live_evaluation_survey.php")
    Call<BasicPojo> addLiveEvaluationSurvey(@Field("user_id") String userId,
                                            @Field("prefix") String prefix,
                                            @Field("q1") String q1,
                                            @Field("q2") String q2,
                                            @Field("q3") String q3,
                                            @Field("q4") String q4,
                                            @Field("q5") String q5,
                                            @Field("q6") String q6,
                                            @Field("q7") String q7,
                                            @Field("q8") String q8,
                                            @Field("q9") String q9,
                                            @Field("q10") String q10,
                                            @Field("q11") String q11,
                                            @Field("q12") String q12);

    @FormUrlEncoded
    @POST("insert_eat_evaluation_survey.php")
    Call<BasicPojo> addEatEvaluationSurvey(@Field("user_id") String userId,
                                            @Field("prefix") String prefix,
                                            @Field("q1") String q1,
                                            @Field("q2") String q2,
                                            @Field("q3") String q3,
                                            @Field("q4") String q4,
                                            @Field("q5") String q5,
                                            @Field("q6") String q6,
                                            @Field("q7") String q7,
                                            @Field("q8") String q8,
                                            @Field("q9") String q9,
                                            @Field("q10") String q10,
                                            @Field("q11") String q11,
                                            @Field("q12") String q12,
                                            @Field("q13") String q13,
                                            @Field("q14") String q14,
                                            @Field("q15") String q15,
                                            @Field("q16") String q16);
    @FormUrlEncoded
    @POST("insert_active_evaluation_survey.php")
    Call<BasicPojo> addActiveEvaluationSurvey(@Field("user_id") String userId,
                                            @Field("prefix") String prefix,
                                            @Field("q1") String q1,
                                            @Field("q2") String q2,
                                            @Field("q3") String q3,
                                            @Field("q4") String q4,
                                            @Field("q5") String q5,
                                            @Field("q6") String q6,
                                            @Field("q7") String q7,
                                            @Field("q8") String q8,
                                            @Field("q9") String q9,
                                            @Field("q10") String q10,
                                            @Field("q11") String q11);
}
