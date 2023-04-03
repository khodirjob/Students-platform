export interface IApartment {
  id?: number;
  location?: string | null;
  phone?: string | null;
  description?: string | null;
  price?: number | null;
  requirements?: string | null;
  roomFit?: number | null;
}

export const defaultValue: Readonly<IApartment> = {};
