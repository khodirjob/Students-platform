import { TranslatorContext, Storage } from 'react-jhipster';

import { setLocale } from 'app/shared/reducers/locale';

TranslatorContext.setDefaultLocale('uz-Latn-uz');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  en: { name: 'English' },
  ru: { name: 'Русский' },
  'uz-Cyrl-uz': { name: 'Ўзбекча' },
  'uz-Latn-uz': { name: 'O`zbekcha' },
  // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'uz-Latn-uz')));
};
