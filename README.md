# GitHub Bot


GitHub's webhook feature notifies this app of any events of interest, such as pull request comments.

To deploy the app:
1. Setup a target repository, enter its name in application.properties, e.g. danielkpl2/github-bot
2. Generate a personal access token in github settings, enter it in application.properties after oauth
3. Deploy the app on a publicly accessible url

	To deploy on heroku:
	1. Create account on heroku
	2. Install and login to heroku https://devcenter.heroku.com/articles/getting-started-with-java#set-up
	3. Clone my app: git clone https://github.com/danielkpl2/github-bot.git (remember to change  application.properties and do git add and commit)
	4. In bash or cmd from the project directory execute: heroku create, note down the url
	5. git push heroku master
	6. heroku ps:scale web=1
	7. heroku logs --tail, if you want to see the log
4. Create a webhook for the target repository and point it to the url above (https://github.com/{username}/{repo}/settings/hooks)

The app will now continuously monitor pull request comments and respond when given the command.
