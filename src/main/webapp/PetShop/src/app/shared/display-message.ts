import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class DisplayMessage {
  displaySuccess(successMessage: string) {
    Swal.fire({
      icon: 'success',
      text: `${successMessage}`,
    });
  }

  displayError(errorMessage: string) {
    Swal.fire({
      icon: 'error',
      text: `${errorMessage}`,
    });
  }
}
