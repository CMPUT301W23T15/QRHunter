const admin = require("firebase-admin");
const functions = require("firebase-functions");
const humanReadableIds = require("human-readable-ids").hri;

admin.initializeApp();

const db = admin.firestore();

exports.createCodeFromPost = functions.firestore
  .document("posts/{postId}")
  .onCreate(async (snap, context) => {
    const postData = snap.data();
    const codeData = postData.code;
    const codesRef = db.collection("codes");

    // Check if a code has already been posted
    const query = codesRef.where("hash", "==", codeData.hash);
    const existingCodes = await query.get();

    let codeName;

    if (existingCodes.empty) {
      // No existing code, generate a new human-readable name
      codeName = await generateUniqueName(codesRef);
      console.log(`Generated new code name: ${codeName}`);

      // Create a new code document with the generated name
      codesRef.add({
        hash: codeData.hash,
        score: codeData.score,
        name: codeName,
      });
    } else {
      // Use the name of the first existing code document
      const existingCodeDoc = existingCodes.docs[0];
      codeName = existingCodeDoc.data().name;
      console.log(`Reusing existing code name: ${codeName}`);
    }

    // Update the name field of the code in the new post document
    const postRef = snap.ref;
    await postRef.update({ name: codeName });
    console.log(
      `Updated name field of code in post ${postRef.id} to ${codeName}`
    );

    return null;
  });

async function generateUniqueName(codesRef) {
  const codeName = humanReadableIds.random();
  const query = codesRef.where("name", "==", codeName);
  const existingCodes = await query.get();
  if (existingCodes.empty) {
    return codeName;
  } else {
    return generateUniqueName(codesRef);
  }
}
