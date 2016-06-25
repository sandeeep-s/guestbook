package com.liferay.docs.guestbook.portlet;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.service.EntryLocalServiceUtil;
import com.liferay.docs.guestbook.service.GuestbookLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class GuestbookPortlet
 */
public class GuestbookPortlet extends MVCPortlet {

	public void addGuestbook(ActionRequest actionRequest, ActionResponse actionResponse)
			throws PortalException, SystemException {
		ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), actionRequest);

		String name = ParamUtil.getString(actionRequest, "name");

		try {
			GuestbookLocalServiceUtil.addGuestbook(serviceContext.getUserId(), name, serviceContext);
			SessionMessages.add(actionRequest, "guestbookAdded");
		} catch (Exception e) {
			SessionErrors.add(actionRequest, e.getClass().getName());
			actionResponse.setRenderParameter("mvcPath", "/html/guestbook/edit_guestbook.jsp");
		}

	}

	public void addEntry(ActionRequest actionRequest, ActionResponse actionResponse)
			throws PortalException, SystemException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(Entry.class.getName(), actionRequest);

		String userName = ParamUtil.getString(actionRequest, "name");
		String email = ParamUtil.getString(actionRequest, "email");
		String message = ParamUtil.getString(actionRequest, "message");
		long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");
		long entryId = ParamUtil.getLong(actionRequest, "entryId");

		if (entryId > 0) {
			try {
				EntryLocalServiceUtil.updateEntry(serviceContext.getUserId(), guestbookId, entryId, userName, email,
						message, serviceContext);
				SessionMessages.add(actionRequest, "entryUpdated");

				actionResponse.setRenderParameter("guestbookId", Long.toString(guestbookId));

			} catch (Exception e) {
				SessionErrors.add(actionRequest, e.getClass().getName());
				PortalUtil.copyRequestParameters(actionRequest, actionResponse);
				actionResponse.setRenderParameter("mvcPath", "/html/guestbook/edit_entry.jsp");
				e.printStackTrace();
			}
		} else {
			try {
				EntryLocalServiceUtil.addEntry(serviceContext.getUserId(), guestbookId, userName, email, message,
						serviceContext);
				SessionMessages.add(actionRequest, "entryAdded");

				actionResponse.setRenderParameter("guestbookId", Long.toString(guestbookId));

			} catch (Exception e) {
				SessionErrors.add(actionRequest, e.getClass().getName());
				PortalUtil.copyRequestParameters(actionRequest, actionResponse);
				actionResponse.setRenderParameter("mvcPath", "/html/guestbook/edit_entry.jsp");
				e.printStackTrace();
			}
		}

	}

	public void deleteEntry(ActionRequest actionRequest, ActionResponse actionResponse){
	    long entryId = ParamUtil.getLong(actionRequest, "entryId");
	    long guestbookId = ParamUtil.getLong(actionRequest, "guestbookId");

	    try {
	    	ServiceContext serviceContext = ServiceContextFactory.getInstance(Entry.class.getName(), actionRequest);
	    	EntryLocalServiceUtil.deleteEntry(entryId, serviceContext);
	    	actionResponse.setRenderParameter("guestbookId", String.valueOf(guestbookId));
			
		} catch (Exception e) {
		}
	}
	
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(Guestbook.class.getName(), renderRequest);

			long groupId = serviceContext.getScopeGroupId();
			long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

			List<Guestbook> guestbooks = GuestbookLocalServiceUtil.getGuestbooks(groupId);

			if (guestbooks.size() == 0) {
				Guestbook guestbook = GuestbookLocalServiceUtil.addGuestbook(serviceContext.getUserId(), "Main",
						serviceContext);
				guestbookId = guestbook.getGuestbookId();
			}

			if (guestbookId <= 0) {
				guestbookId = guestbooks.get(0).getGuestbookId();
			}

			renderRequest.setAttribute("guestbookId", guestbookId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new PortletException();
		}

		super.render(renderRequest, renderResponse);

	}

}
