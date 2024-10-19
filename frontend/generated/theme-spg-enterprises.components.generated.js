import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import vaadinTextFieldCss from 'themes/spg-enterprises/components/vaadin-text-field.css?inline';
import vaadinTextAreaCss from 'themes/spg-enterprises/components/vaadin-text-area.css?inline';
import vaadinPasswordFieldCss from 'themes/spg-enterprises/components/vaadin-password-field.css?inline';
import titlePanelCss from 'themes/spg-enterprises/components/title-panel.css?inline';
import formViewCss from 'themes/spg-enterprises/components/form-view.css?inline';
import employeeGridCss from 'themes/spg-enterprises/components/employee-grid.css?inline';
import cardListCss from 'themes/spg-enterprises/components/card-list.css?inline';


if (!document['_vaadintheme_spg-enterprises_componentCss']) {
  registerStyles(
        'vaadin-text-field',
        unsafeCSS(vaadinTextFieldCss.toString())
      );
      registerStyles(
        'vaadin-text-area',
        unsafeCSS(vaadinTextAreaCss.toString())
      );
      registerStyles(
        'vaadin-password-field',
        unsafeCSS(vaadinPasswordFieldCss.toString())
      );
      registerStyles(
        'title-panel',
        unsafeCSS(titlePanelCss.toString())
      );
      registerStyles(
        'form-view',
        unsafeCSS(formViewCss.toString())
      );
      registerStyles(
        'employee-grid',
        unsafeCSS(employeeGridCss.toString())
      );
      registerStyles(
        'card-list',
        unsafeCSS(cardListCss.toString())
      );
      
  document['_vaadintheme_spg-enterprises_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

