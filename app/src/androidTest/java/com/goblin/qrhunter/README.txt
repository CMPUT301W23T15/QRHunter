IMPORTANT NOTES IN TESTING

Should test firebase folder with: firebase emulators:start -> then run the firebase folder for tests
(assuming firebase login is already in)

RUN THE UI FOLDER TESTS SEPERATELY WITHOUT FIREBASE -> as firebase might conflict with the robotium
solo user with an actual firebase user, causing issues with the profile tests.

If any issues when running, please email aswu@ualberta.ca for further explanation.

UI Intent Tests
if when run the folder, and leaderBoardFragmentTest fails due to binding issue,
just run the folder again because this is an emulator problem.
If having random issues, please run MainActivity (not the test) once and then run the tests.

- Alvin wu