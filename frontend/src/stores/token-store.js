import { defineStore } from 'pinia';
import { LocalStorage } from 'quasar';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    isAuthenticated: false
  }),
  getters: {
    getIsAuthenticated: state => LocalStorage.getItem('isAuthenticated')
  },
  actions: {
    updateIsAuthenticated() {
      this.isAuthenticated = true;
      LocalStorage.set('isAuthenticated', this.isAuthenticated);
    }
  }
});
