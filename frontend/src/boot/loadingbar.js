import { boot } from 'quasar/wrappers';
import { LoadingBar } from 'quasar';

export default boot(async ({ app, router, store }) => {
  LoadingBar.setDefaults({
    color: '#1976d2',
    size: '4px',
    position: 'top'
  });
});
