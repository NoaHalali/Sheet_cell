package utils;


import shticell.chat.ChatManager;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.exceptions.OutdatedSheetVersionException;
import shticell.files.FileManager;
import shticell.sheets.manager.MultiSheetEngineManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import shticell.users.UserManager;

import static constant.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	//private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
	private static final  String FILE_MANAGER_ATTRIBUTE_NAME = "fileManager";
	private static final String MULTI_SHEET_ENGINE_MANAGER_ATTRIBUTE_NAME = "multiSheetEngineManager";
	private static final String  CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object fileManagerLock = new Object();
	private static final Object multiSheetEngineManagerLock = new Object();
	private static final Object chatManagerLock = new Object();
	//private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static FileManager getFileManager(ServletContext servletContext) {
		synchronized (fileManagerLock) {
			if (servletContext.getAttribute(FILE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(FILE_MANAGER_ATTRIBUTE_NAME, new FileManager());
			}
		}
		return (FileManager) servletContext.getAttribute(FILE_MANAGER_ATTRIBUTE_NAME);
	}
	public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
			return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);

		}
	}


	public static MultiSheetEngineManager getMultiSheetEngineManager(ServletContext servletContext) {
		synchronized (multiSheetEngineManagerLock) {
			if (servletContext.getAttribute(MULTI_SHEET_ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(MULTI_SHEET_ENGINE_MANAGER_ATTRIBUTE_NAME, new MultiSheetEngineManager());
			}
		}
		return (MultiSheetEngineManager) servletContext.getAttribute(MULTI_SHEET_ENGINE_MANAGER_ATTRIBUTE_NAME);
	}

	public static SheetEngine getSheetEngineByName(String sheetName, ServletContext servletContext) throws IllegalArgumentException{
		MultiSheetEngineManager engineManager = getMultiSheetEngineManager(servletContext);
		return engineManager.getSheetEngine(sheetName);
	}

	public static void checkIfClientSheetVersionIsUpdated(HttpServletRequest request, SheetEngine sheetEngine) throws OutdatedSheetVersionException {
		String userVersion = SessionUtils.getViewedSheetVersion(request);
		sheetEngine.checkIfVersionIsUpdated(userVersion);
	}
	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}


	//	public static int getIntParameter(HttpServletRequest request, String name) {
//		String value = request.getParameter(name);
//		if (value != null) {
//			try {
//				return Integer.parseInt(value);
//			} catch (NumberFormatException numberFormatException) {
//			}
//		}
//		return INT_PARAMETER_ERROR;
//	}


}
