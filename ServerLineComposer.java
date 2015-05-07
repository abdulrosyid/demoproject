package com.sdd.demoproject.chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;

import com.sdd.demoproject.DAO.TproduksiDAO;
import com.sdd.demoproject.domain.Mipp;
import com.sdd.demoproject.domain.Tproduksi;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class ServerLineComposer {
	@Wire("#chart")
	Charts chart1;

	@Wire("#Customer")
	Groupbox Gbcustomer;

	@Wire("#gridcust")
	Grid gridcust;

	// @Wire
	// private Timer timer;

	TproduksiDAO daoProduksi = new TproduksiDAO();

	/*
	 * public void doAfterCompose(Window comp) throws Exception {
	 * super.doAfterCompose(comp); buildData(); }
	 */

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {

			final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, ''yy");

			String stFilter = "";
			
			List<Tproduksi> listProduksi = new ArrayList<>();
			
			listProduksi = daoProduksi.list(stFilter);
			
			gridcust.setModel(new ListModelList<>(listProduksi));

			gridcust.setRowRenderer(new RowRenderer<Tproduksi>() {

				@Override
				public void render(Row row, final Tproduksi data, int index)
						throws Exception {

					String tglProduksi = sdf.format(data.getDate_produksi());

					Mipp viewMipp = data.getMipp();

					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(viewMipp.getNama_ipp()));
					row.getChildren().add(new Label(tglProduksi));
					row.getChildren().add(
							new Label(data.getStock_produksi().toString()));
					row.getChildren().add(
							new Label(data.getStock_power_plant().toString()));
				}
			});

			buildData(listProduksi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// timer.setDelay(1000 * 5);
		// timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
		// @Override
		// public void onEvent(Event event) throws Exception {
		// buildData();
		// }
		// });
	}

	/*
	 * public void onTimer() { buildData(); }
	 */

	public void buildData(List<Tproduksi> listProduksi) throws Exception {

		System.out.println("buildData");

		CategoryModel catemodel = new DefaultCategoryModel();

		final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, ''yy");

		String stPerusahaanIpp = "", tglProduksi = "";

		for (Tproduksi produksi : listProduksi) {
			Mipp mipp = produksi.getMipp();
			stPerusahaanIpp = mipp.getNama_ipp();
			tglProduksi = sdf.format(produksi.getDate_produksi());
			catemodel.setValue(stPerusahaanIpp, tglProduksi,
					produksi.getStock_produksi());
			System.out.println("stPerusahaanIpp : " + stPerusahaanIpp
					+ ", tglProduksi : " + tglProduksi);
		}

		chart1.setModel(catemodel);

		chart1.getXAxis().setMin(0);
		chart1.getXAxis().getTitle().setText("MW (Mega Watt)");

		Tooltip tooltip = chart1.getTooltip();
		tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>");
		tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>"
				+ "<td style=\"padding:0\"><b>{point.y:.1f} MW</b></td></tr>");
		tooltip.setFooterFormat("</table>");
		tooltip.setShared(true);
		tooltip.setUseHTML(true);

		chart1.getPlotOptions().getColumn().setPointPadding(0.2);
		chart1.getPlotOptions().getColumn().setBorderWidth(0);

	}
}
