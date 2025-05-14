package com.pearl.propertiesApp.Utilities;

public class MailTemplates {

    private static String getHeader(String title) {

        return String.format(
                """
                                <div style="text-align: left;">
                                <img src="https://res.cloudinary.com/dvp0fow8r/image/upload/v1747220412/Untitled_pth3y3.png"
                        alt="Logo" style="max-width: 100px; height: auto; margin-bottom: 10px;">
                                </div>""" +
                        "<div style='background-color:#f8f9fa;" +
                        "padding:20px 0;" +
                        "text-align:center;" +
                        "font-family:Arial,sans-serif;'>"
                        + "<h1 style='color:#343a40;'>%s</h1>"
                        + "</div>", title);
    }

    private static String getFooter() {

        return "<div style='text-align:center;padding:20px;" +
                "background-color:#f8f9fa;" +
                "font-family:Arial,sans-serif;'>"
                + "<p style='color:#6c757d;'>Thank you for choosing Property APP!</p>"
                + "<p style='color:#6c757d;'>The Property APP Team</p>"
                + "</div>"
                + """
                        <div style="text-align: center;">
                        <img src="https://res.cloudinary.com/dvp0fow8r/image/upload/v1747220412/Untitled_pth3y3.png"
                alt="Logo" style="max-width: 100px; height: auto; margin-bottom: 10px;">
                        </div>""";
    }

    public static String OTP(String otp) {

        return getHeader("Property APP Account OTP") +
                "<div style='padding: 20px; font-family: Arial, sans-serif; color: #333;'>"
                + "<p>Dear Customer<strong></strong>,</p>"
                + "<p>Please verify your account by using the OTP below:</p>"
                + "<p style='font-size: 24px; font-weight: bold; color: #007bff; margin: 10px 0;'>" + otp + "</p>"
                + "<p>Do not share the OTP with anyone.</p>"
                + "</div>" + getFooter();
    }

    public static String registrationEmail(String userName, String otp) {
        return getHeader("Welcome to Property APP!") +
                "<div style='padding: 20px; font-family: Arial, sans-serif; color: #333;'>"
                + "<p>Dear <strong>" + userName + "</strong>,</p>"
                + "<p>Thank you for registering with us. We're excited to have you on board!</p>"
                + "<p>Please verify your account by using the OTP below:</p>"
                + "<p style='font-size: 24px; font-weight: bold; color: #007bff; margin: 10px 0;'>" + otp + "</p>"
                + "<p>If you have any questions, feel free to reach out to our support team.</p>"
                + "</div>" + getFooter();
    }

    public static String passwordReset() {
        return getHeader("Property APP password reset Successfully!!") +
                "<div style='padding: 20px; font-family: Arial, sans-serif; color: #333;'>"
                + "<p>Your account has been recovered Successfully!</p>"
                + "<p>If you don't recognise this activity, Contact Support Immediately</p>"
                + "<p>If you have any questions, feel free to reach out to our support team.</p>"
                + "</div>" + getFooter();
    }
}
