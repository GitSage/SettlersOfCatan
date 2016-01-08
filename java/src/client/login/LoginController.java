package client.login;

import client.base.*;
import client.misc.*;

import client.proxy.ProxyFacade;
import shared.request.game.UserRequest;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
//	private ProxyFacade facade;

	/**
	 * LoginController constructor
	 *
	 * @param view        Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {
		super(view);
		this.messageView = messageView;
	}

	public ILoginView getLoginView() {
		return (ILoginView) super.getView();
	}

	public IMessageView getMessageView() {
		return messageView;
	}

	/**
	 * Sets the action to be executed when the user logs in
	 *
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		loginAction = value;
	}

	/**
	 * Returns the action to be executed when the user logs in
	 *
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		return loginAction;
	}

	@Override
	public void start() {
		getLoginView().showModal();
	}

	@Override
	public void signIn() {
		UserRequest userRequest = new UserRequest(getLoginView().getLoginUsername(), getLoginView().getLoginPassword());
		// If log in succeeded
		if (ProxyFacade.getInstance().login(userRequest) == 1) {
			getLoginView().closeModal();
			loginAction.execute();
		} else {
			messageView.setTitle("Error");
			messageView.setMessage("Login failed.");
			messageView.showModal();
		}
	}

	@Override
	public void register() {
		String validUsername = "^[a-zA-Z0-9_\\-]{3,7}$";
		String validPassword = "^[a-zA-Z0-9_\\-]{5,}$";
		String username = getLoginView().getRegisterUsername();
		String password = getLoginView().getRegisterPassword();
		if (!password.equals(getLoginView().getRegisterPasswordRepeat())) {
			messageView.setTitle("Error");
			messageView.setMessage("Passwords do not match");
			messageView.showModal();
		} else if (!username.matches(validUsername)) {
			messageView.setTitle("Error");
			messageView.setMessage("Username must match this regular expression: " + validUsername);
			messageView.showModal();
		} else if (!password.matches(validPassword)) {
			messageView.setTitle("Error");
			messageView.setMessage("Password must match this regular expression: " + validPassword);
			messageView.showModal();
		} else {
			UserRequest userRequest = new UserRequest(username, password);
			// If log in succeeded
			if (ProxyFacade.getInstance().register(userRequest) == 1) {
				getLoginView().closeModal();
				loginAction.execute();
			} else {
				messageView.setTitle("Failure");
				messageView.setMessage("Could not register. Are you sure you're connected to the server?");
				messageView.showModal();
			}
		}
	}

}

