package nl.bosseur.beachvolleybal;

import nl.bosseur.beachvolleybal.activity.BeachVolleyBallDelegate;

/**
 * Created by ekoetsier on 26/10/2016.
 */

public enum ExecutionStateEnum {
	START {
		@Override
		public void execute(BeachVolleyBallDelegate delegate) {
			delegate.executeTask();
		}
	},
	RUNNING {
		@Override
		public void execute(BeachVolleyBallDelegate delegate) {
			delegate.showProgress("Loading");
		}
	},
	RECEIVED {
		@Override
		public void execute(BeachVolleyBallDelegate delegate) {
			delegate.hideProgress();
			delegate.showResults();
		}
	};

	public abstract void execute(BeachVolleyBallDelegate delegate);
}
