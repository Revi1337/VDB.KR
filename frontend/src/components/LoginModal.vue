<template>
  <q-dialog @update:model-value="layout" @hide="resetData">
    <q-card
      flat
      class="login-container row items-center justify-center q-px-sm q-pt-md"
    >
      <q-card-section>
        <HeaderLogo />
      </q-card-section>

      <q-form @submit.prevent="userLoginRequest">
        <div class="login-component">
          <div>
            <Input
              autofocus
              v-model="signInData.email"
              outlined
              label="Email"
              :spellcheck="false"
            />
            <Input
              v-model="signInData.password"
              outlined
              label="Password"
              :spellcheck="false"
              type="password"
            />

            <div style="height: 20px">
              <div class="q-ml-md text-red block float-left" ref="shakeMessage">
                {{ errorMessage }}
              </div>
            </div>
          </div>

          <div class="q-mb-md q-mt-md row justify-between items-center">
            <div class="q-gutter-y-sm block">
              <div
                class="login-options text-darkpurple"
                @click="movePage('Forgot')"
              >
                Forgot your password?
              </div>
              <div
                class="login-options text-darkpurple"
                @click="movePage('SignUp')"
              >
                Don't have an account?
              </div>
            </div>
            <Button outline type="submit" label="Sign In" />
          </div>

          <q-separator inset />

          <div class="q-mt-sm q-gutter-y-md q-pb-xl">
            <Button
              class="login-component"
              color="primary"
              :ripple="false"
              outline
              href="/api/oauth2/google"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="primary"
                  size="16px"
                  name="fa-brands fa-google"
                />
                <span class="text-primary">sign in google</span>
              </div>
            </Button>

            <Button
              class="login-component"
              color="primary"
              :ripple="false"
              outline
              href="/api/oauth2/github"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="primary"
                  size="16px"
                  name="fa-brands fa-github"
                />
                <span class="text-primary">sign in github</span>
              </div>
            </Button>
          </div>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script setup>
import Button from 'src/components/Button.vue';
import HeaderLogo from './HeaderLogo.vue';
import { ref } from 'vue';
import { userLogin } from 'src/api/auth';
import { useRoute, useRouter } from 'vue-router';
import Input from './Input.vue';

const router = useRouter();
const route = useRoute();

const emit = defineEmits(['signIn']);
const props = defineProps({
  layout: {
    type: Boolean
  }
});

const shakeMessage = ref(null);
const errorMessage = ref('');
const signInData = ref({
  email: '',
  password: ''
});
const userLoginRequest = async () => {
  try {
    const { data } = await userLogin(signInData.value);
    console.log('success');
    emit('signIn');
    router.push({ name: route.name });
  } catch (error) {
    errorMessage.value = error.response.data.error.message;
    shakeMessage.value.classList.add('vibration');
    setTimeout(() => {
      shakeMessage.value.classList.remove('vibration');
    }, 400);
  }
};

const resetData = () => {
  errorMessage.value = '';
  signInData.value.email = '';
  signInData.value.password = '';
};

const movePage = path => {
  emit('signIn');
  router.push({ name: path });
};
</script>

<style lang="scss" scoped>
.text-darkpurple {
  // #CD31FF
  color: #926dff !important;
}
.login-container {
  width: 500px;
  border-radius: 8px;
}
.login-component {
  width: 350px;
}

.vibration {
  animation: vibration 0.02s infinite;
}
@keyframes vibration {
  from {
    transform: translateX(-0.6%);
  }
  to {
    transform: translateX(0.6%);
  }
}

.login-options {
  position: relative;
  text-decoration: none;
  cursor: pointer;

  &:before {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 0;
    width: 0;
    height: 2px;
    background-color: $darkpurple;
    transition: width 0.3s ease-in-out;
  }

  &:hover:before {
    width: 100%;
  }
}
</style>
