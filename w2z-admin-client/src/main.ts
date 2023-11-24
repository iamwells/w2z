import { createApp } from 'vue';
import './style.css';
import App from './App.vue';

import router from './router';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

import { Quasar } from 'quasar';
import '@quasar/extras/material-icons/material-icons.css';
import 'quasar/dist/quasar.css';

const app = createApp(App);

app.use(router);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);

app.use(Quasar, {
  plugins: {}, // import Quasar plugins and add here
});

app.mount('#app');
