import { Pet } from "./pet";

export interface Hero {
  id: number;
  name: string;
  pet?: Pet;
}
