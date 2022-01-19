## Git hooks
_Please keep going, no fancy quote about hooks here :P._

### Pre-requisites
* Verify that you have Java 8 in your system.

### Installation
* Run a build or clean the project. Easy peasy ;)
* You can run `./gradlew installGitHooks` manually to install them.

### How to use
The scripts will be automatically executed before committing code into the local repository.
There is one hook that will be called when we want to commit code into the repo:

* The __pre-commit__ hook will be called before typing any commit message and validates that the
currently staged _.kt_ files pass our _detekt_ rules. __The commit will be rejected 
if any of the staged files does not pass the defined rules__.

* The __commit-msg__ hook is executed just after the user has typed the commit message. 
This hook __validates that the commit message contains a valid issue tag prefix for the current 
project__ (in this case, it tries to find the regex _(TBD-[0-9]+) (.*)_).
If the message does not comply with this format, we will have to confirm if we really want to 
perform the commit.