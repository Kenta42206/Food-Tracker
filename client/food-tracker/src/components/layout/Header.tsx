import { Alignment, Button, Navbar } from "@blueprintjs/core";
import React from "react";

const Header = () => {
  return (
    <Navbar>
      <Navbar.Group align={Alignment.LEFT}>
        <Navbar.Heading>Blueprint</Navbar.Heading>
        <Navbar.Divider />
        <Button className="bp5-minimal" icon="home" text="Home" />
        <Button className="bp5-minimal" icon="document" text="Files" />
      </Navbar.Group>
    </Navbar>
  );
};

export default Header;
