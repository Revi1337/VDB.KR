<template>
  <q-layout view="lHh Lpr lFf">
    <PageHeader>
      <template #header-content>
        <DropDown class="q-ml-lg" label="Products">
          <DropDownItem :contents="[{ label: 'LABEL1', clickable: true }]" />
        </DropDown>

        <DropDown label="Resources">
          <DropDownItem
            :contents="[
              { label: 'LABEL1', clickable: true },
              { label: 'LABEL2', clickable: false }
            ]"
          />
        </DropDown>

        <Button flat label="statistics" to="Statistic" />
        <Button flat label="statistics2" to="Statistic2" />
      </template>

      <template #header-left>
        <Button label="Log In" outline @click="layout = !layout" />
        <Button label="Sign Up" to="SignUp" />
        <LoginModal v-model="layout" @sign-in="layout = !layout" />
      </template>
    </PageHeader>

    <PageContent />
    <PageFooter />
  </q-layout>
</template>

<script setup>
import PageHeader from 'src/components/PageHeader.vue';
import PageContent from 'src/components/PageContent.vue';
import PageFooter from 'src/components/PageFooter.vue';
import DropDown from 'src/components/DropDown.vue';
import DropDownItem from 'src/components/DropDownItem.vue';
import Button from 'src/components/Button.vue';
import LoginModal from 'src/components/LoginModal.vue';
import { ref } from 'vue';

const layout = ref(false);
</script>

<style lang="scss" scoped></style>
