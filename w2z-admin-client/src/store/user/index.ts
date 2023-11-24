import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
  const user = ref<User>({
    username: '',
  });

  const saveUser = (user: User) => {};

  return {
    user,
    saveUser,
  };
});

export type User = {
  id?: number;
  username: string;
  password?: string;
  nickname?: string;
  gender?: string;
  birthday?: string;
  email?: string;
  phoneNumber?: string;
  create_time?: string;
  update_time?: string;
  authorities?: string[];
};
