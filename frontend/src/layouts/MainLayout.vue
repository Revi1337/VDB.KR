<template>
  <q-layout view="lHh Lpr lFf">
    <HeaderComponent>
      <template #header-content>
        <DropDownComponent class="q-ml-lg" label="Products">
          <DropDownItemComponent
            :contents="[{ label: 'LABEL1', clickable: true }]"
          />
        </DropDownComponent>

        <DropDownComponent label="Resources">
          <DropDownItemComponent
            :contents="[
              { label: 'LABEL1', clickable: true },
              { label: 'LABEL2', clickable: false }
            ]"
          />
        </DropDownComponent>

        <ButtonComponent flat label="statistics" to="Statistic" />
        <ButtonComponent flat label="statistics2" to="Statistic2" />
      </template>

      <template #header-left>
        <ButtonComponent label="Log In" outline @click="layout = !layout" />
        <ButtonComponent label="Sign Up" to="SignUp" />
        <LoginModalComponent v-model="layout" />
      </template>
    </HeaderComponent>

    <ContentComponent />
    <FooterComponent />
  </q-layout>
</template>

<script setup>
import HeaderComponent from 'src/components/HeaderComponent.vue';
import ContentComponent from 'src/components/ContentComponent.vue';
import FooterComponent from 'src/components/FooterComponent.vue';
import DropDownComponent from 'src/components/DropDownComponent.vue';
import DropDownItemComponent from 'src/components/DropDownItemComponent.vue';
import ButtonComponent from 'src/components/ButtonComponent.vue';
import LoginModalComponent from 'src/components/LoginModalComponent.vue';
import { ref } from 'vue';

const layout = ref(false);
</script>

<style lang="scss" scoped></style>
