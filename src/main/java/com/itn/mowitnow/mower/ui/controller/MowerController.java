package com.itn.mowitnow.mower.ui.controller;

import static org.springframework.util.CollectionUtils.isEmpty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itn.mowitnow.mower.beans.InfoGameBean;
import com.itn.mowitnow.mower.beans.MowerBean;
import com.itn.mowitnow.mower.beans.PositionBean;
import com.itn.mowitnow.mower.services.impl.MowerMouvementServiceImpl;

@Controller
public class MowerController {

	@RequestMapping("/rungame")
	public String resultgame(Model model) throws Exception {
		model.addAttribute("resultat", getResultRunGame());
		return "resultgame";
	}

	/**
	 * Appel au service pour la lecture du fichier et execution du jeu
	 * @return le résultat du jeu
	 */
	private String getResultRunGame() {
		StringBuilder sb = new StringBuilder();
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		MowerMouvementServiceImpl mouvementService = ctx.getBean(MowerMouvementServiceImpl.class);

		InfoGameBean infoGameBean;
		try {
			infoGameBean = mouvementService.loadDataFromFile();
			if (infoGameBean != null && !isEmpty(infoGameBean.getTondeuses())) {
				for (MowerBean mowerBean : infoGameBean.getTondeuses()) {
					mouvementService.moveMower(new PositionBean(infoGameBean.getX(), infoGameBean.getY()), mowerBean);
					sb.append(" X :").append(mowerBean.getX()).append(" Y :").append(mowerBean.getY())
							.append(" Orientation " + ":").append(mowerBean.getOrientation());
				}
			}
		} catch (Exception e) {
			sb.append("Une erreur est survenue. Vérifiez le format de votre fichier !!! ");
		}
		return sb.toString();
	}

}
