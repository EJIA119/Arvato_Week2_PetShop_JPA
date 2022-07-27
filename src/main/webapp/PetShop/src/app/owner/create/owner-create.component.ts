import { DatePipe, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { IOwner } from '../owner';
import { OwnerService } from '../owner.service';

@Component({
  selector: 'app-owner-create',
  templateUrl: './owner-create.component.html',
  styleUrls: ['./owner-create.component.css'],
  providers: [DatePipe],
})
export class OwnerCreateComponent implements OnInit {
  pageTitle = 'Create Owner';
  newOwner!: IOwner;
  errorMessage: string = '';

  constructor(
    private ownerService: OwnerService,
    private datePipe: DatePipe,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.newOwner = {
      id: 0,
      firstName: '',
      lastName: '',
      dateCreated: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || '',
      date_modified: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || '',
    };
  }

  createOwnerForm = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
  });

  onSubmit() {
    this.newOwner.firstName =
      this.createOwnerForm.get('firstName')?.value?.trim() || '';
    this.newOwner.lastName =
      this.createOwnerForm.get('lastName')?.value?.trim() || '';

    this.ownerService.addOwner(this.newOwner).subscribe({
      error: (err) => (this.errorMessage = err),
    });

    if (this.errorMessage == '') {
      Swal.fire({
        icon: 'success',
        title: 'New owner created!',
      }).then((result) => {
        this.createOwnerForm.reset();
      });
    }
  }

  navigateBack() {
    this.location.back();
    // this.router.navigate(['/home']);
  }
}
