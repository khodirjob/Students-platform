import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/book">
        <Translate contentKey="global.menu.entities.book" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/authors">
        <Translate contentKey="global.menu.entities.authors" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vacancy">
        <Translate contentKey="global.menu.entities.vacancy" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cafe">
        <Translate contentKey="global.menu.entities.cafe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/attachment">
        <Translate contentKey="global.menu.entities.attachment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/food">
        <Translate contentKey="global.menu.entities.food" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/room">
        <Translate contentKey="global.menu.entities.room" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/apartment">
        <Translate contentKey="global.menu.entities.apartment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/schedule">
        <Translate contentKey="global.menu.entities.schedule" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
