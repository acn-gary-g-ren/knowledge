package org.support.project.knowledge.control.api.internal.articles;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.util.StringUtils;
import org.support.project.di.DI;
import org.support.project.di.Instance;
import org.support.project.knowledge.logic.KnowledgeDataEditLogic;
import org.support.project.knowledge.vo.api.KnowledgeDetail;
import org.support.project.web.bean.NameId;
import org.support.project.web.boundary.Boundary;
import org.support.project.web.common.HttpStatus;
import org.support.project.web.control.ApiControl;
import org.support.project.web.control.service.Put;
import org.support.project.web.exception.InvalidParamException;

import net.arnx.jsonic.JSONException;

@DI(instance = Instance.Prototype)
public class PutArticleApiControl extends ApiControl {
    /** ログ */
    private static final Log LOG = LogFactory.getLog(PutArticleApiControl.class);
    /**
     * 記事を投稿(Release)
     * @throws Exception 
     */
    @Put(path="_api/articles/:id", checkCookieToken=false, checkHeaderToken=true)
    public Boundary article() throws Exception {
        LOG.trace("access user: " + getLoginUserId());
        try {
            String id = getAttributeByString("id"); // パスの :id は attribute にセットしている
            if (!StringUtils.isLong(id)) {
                return sendError(HttpStatus.SC_400_BAD_REQUEST);
            }
            KnowledgeDetail data = getJsonObject(KnowledgeDetail.class);
            data.setKnowledgeId(new Long(id));
            KnowledgeDataEditLogic.get().update(data, getLoginedUser());
            return send(HttpStatus.SC_200_OK, new NameId(data.getTitle(), String.valueOf(data.getKnowledgeId())));
        } catch (JSONException e) {
            LOG.debug("json parse error", e);
            return sendError(HttpStatus.SC_400_BAD_REQUEST);
        } catch (InvalidParamException e) {
            return sendError(e);
        } catch (Exception e) {
            LOG.error("error", e);
            return sendError(HttpStatus.SC_500_INTERNAL_SERVER_ERROR);
        }
    }

}