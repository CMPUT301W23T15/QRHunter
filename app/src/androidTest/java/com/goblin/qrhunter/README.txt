IMPORTANT NOTES IN TESTING

Should test firebase folder with: firebase emulators:start -> then run the firebase folder for tests
(assuming firebase login is already in)

FIREBASE:
if firebase doesn't run / pass, it is not connected.
in terminal in the firebase folder:
- firebase use --add
- use the key buttons (dpad) to stage our project
- then , make a name for the staging alias
- then, firebase emulators:start

RUN THE UI FOLDER TESTS SEPERATELY WITHOUT FIREBASE -> as firebase might conflict with the robotium
solo user with an actual firebase user, causing issues with the profile tests.

If any issues when running, please email aswu@ualberta.ca for further explanation.

UI Intent Tests
if when run the folder, and leaderBoardFragmentTest fails due to binding issue,
just run the folder again because this is an emulator problem.
If having random issues, please run MainActivity (not the test) once and then run the tests.

Start the user off with zero posts, for realistic purpopses. if user has posts when run the tests, please delete
them and run the tests.

Also, please run through main and play around with some of the permissions needed for the app so testing goes
smoothly. else, issues may arise where they need permission for (eg. camera) that robotium did not handle

Non Testable
- there is no way for robotium to be able to access the emulators camera to take picture, so we tested
- up to the point where it can open camera flawlessly

- Alvin wu, email aswu@ualberta.ca for further questions