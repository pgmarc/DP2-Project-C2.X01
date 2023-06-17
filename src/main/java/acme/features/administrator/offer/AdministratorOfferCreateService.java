
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.SystemCurrency;
import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	@Autowired
	protected AdministratorOfferRepository repository;


	@Override
	public void check() {

		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Date instantiationMoment;
		Offer offer;

		instantiationMoment = MomentHelper.getCurrentMoment();

		offer = new Offer();
		offer.setInstantiationMoment(instantiationMoment);

		super.getBuffer().setData(offer);

	}

	@Override
	public void bind(final Offer offer) {

		super.bind(offer, "heading", "summary", "startingDate", "endingDate", "price", "moreInfo");

	}

	@Override
	public void validate(final Offer offer) {

		Money price;
		SystemCurrency systemCurrency;
		String supportedCurrencies;
		List<String> currencies;
		final double minAmount = 0;
		final double maxAmount = 1000000;
		Date minimunAvailability;
		Date maximumAvailability;
		Date baseMoment;
		boolean isAmountBetweenRange;
		boolean isSupportedCurrency;
		boolean isStartingDateBeforeEndingDate;
		boolean isEndingDateBeforeMaximumDate;
		boolean isOneWeekLong;
		final boolean isOneDayAhead;

		price = offer.getPrice();
		systemCurrency = this.repository.showSystemCurrency();
		supportedCurrencies = systemCurrency.getSupportedCurrencies();
		baseMoment = MomentHelper.getBaseMoment();

		currencies = Arrays.asList(supportedCurrencies.trim().split(";"));

		if (!super.getBuffer().getErrors().hasErrors()) {

			isStartingDateBeforeEndingDate = MomentHelper.isBefore(offer.getStartingDate(), offer.getEndingDate());
			super.state(isStartingDateBeforeEndingDate, "*", "company.session.form.error.endingDate-before-startingDate");

			minimunAvailability = MomentHelper.deltaFromMoment(offer.getInstantiationMoment(), 1, ChronoUnit.DAYS);
			isOneDayAhead = MomentHelper.isAfter(offer.getStartingDate(), minimunAvailability);
			super.state(isOneDayAhead, "*", "a");

			maximumAvailability = MomentHelper.deltaFromMoment(baseMoment, 100, ChronoUnit.YEARS);
			isEndingDateBeforeMaximumDate = MomentHelper.isBefore(offer.getStartingDate(), maximumAvailability);
			super.state(isEndingDateBeforeMaximumDate, "*", "company.session.form.error.max-deadline");

			isOneWeekLong = MomentHelper.isLongEnough(offer.getStartingDate(), offer.getEndingDate(), 7, ChronoUnit.DAYS);
			super.state(isOneWeekLong, "*", "company.session.form.error.min-duration");
		}

		if (!super.getBuffer().getErrors().hasErrors("price")) {
			isAmountBetweenRange = minAmount <= price.getAmount() && price.getAmount() <= maxAmount;
			super.state(isAmountBetweenRange, "price", "administrator.offer.form.error.amount-out-of-range");

			isSupportedCurrency = currencies.contains(price.getCurrency());
			super.state(isSupportedCurrency, "price", "administrator.offer.form.error.currency-not-supported");

		}

	}

	@Override
	public void perform(final Offer offer) {

		this.repository.save(offer);
	}

	@Override
	public void unbind(final Offer offer) {

		Tuple tuple;

		tuple = super.unbind(offer, "heading", "summary", "startingDate", "endingDate", "price", "moreInfo");

		super.getResponse().setData(tuple);

	}
}
