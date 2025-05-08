/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendTeamInviteNotification = functions.firestore
  .document("teamInvites/{inviteId}")
  .onCreate(async (snap, context) => {
    const inviteData = snap.data();
    const receiverId = inviteData.receiverId;
    const senderName = inviteData.senderName;
    const eventName = inviteData.eventName;
    const eventId = inviteData.eventId;
    const senderId = inviteData.senderId;

    try {
      const userDoc = await admin
        .firestore()
        .collection("users")
        .doc(receiverId)
        .get();

      const fcmToken = userDoc.data().fcmToken;

      const payload = {
        notification: {
          title: "Team Invite",
          body:
            `${senderName} invited you ` +
            `to join a team for ${eventName}`,
        },
        data: {
          eventId: eventId,
          senderId: senderId,
        },
      };

      await admin.messaging().sendToDevice(fcmToken, payload);
      console.log("Notification sent successfully");
    } catch (error) {
      console.error("Error sending notification:", error);
    }
  });


// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
