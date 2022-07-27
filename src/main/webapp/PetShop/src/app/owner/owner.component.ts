import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { IOwner } from './owner';
import { OwnerService } from './owner.service';
import { OwnerFunction } from './owner-funtions';
import { DatePipe, Location } from '@angular/common';
import { NgModel } from '@angular/forms';
import * as jQuery from 'jquery';

@Component({
  selector: 'app-owner',
  templateUrl: './owner.component.html',
  styleUrls: ['./owner.component.css'],
  providers: [DatePipe]
})
export class OwnerComponent implements OnInit {
  pageTitle = 'Owner Listing';

  ownerList: IOwner[] = [];
  owner!: IOwner;

  errorMessage: string = '';
  sub!: Subscription;

  constructor(
    private ownerService: OwnerService,
    private route: ActivatedRoute,
    private router: Router,
    private ownerFunction: OwnerFunction,
    private datePipe: DatePipe,
    private location: Location
  ) {}

  ngOnInit(): void {

    // try to pass something to this page so that the owner list can be initialize
    // if there is nothing pass in, then just retrieve from database

    if (this.route.snapshot.url[1].path.match('findAll')) {
      this.sub = this.ownerService.findAll().subscribe({
        next: (ownerList) => (this.ownerList = ownerList),
        error: (err) => (this.errorMessage = err),
      });
    } else if (this.route.snapshot.url[1].path.match('findByDateCreated')) {
      const dateCreated = String(
        this.route.snapshot.paramMap.get('dateCreated')
      );

      this.pageTitle += `: Find by Date Created (${dateCreated})`;

      this.sub = this.ownerService.findByDateCreated(dateCreated).subscribe({
        next: (ownerList) => (this.ownerList = ownerList),
        error: (err) => (this.errorMessage = err),
      });

    } else if (this.route.snapshot.url[1].path.match('findByPetName')) {
      const petName = String(this.route.snapshot.paramMap.get('name'));

      this.pageTitle += `: Find by Pet Name (${petName})`;

      this.sub = this.ownerService.findByPetName(petName).subscribe({
        next: (ownerList) => (this.ownerList = ownerList),
        error: (err) => (this.errorMessage = err),
      });
    }
  }

  // Handling Owner API
  owner_findById() {
    this.ownerFunction.owner_findById();
  }

  owner_findPetByOwnerId() {
    this.ownerFunction.owner_findPetByOwnerId();
  }

  owner_findByDateCreated(datePicker : NgModel) {
    let dateCreated = this.datePipe.transform(datePicker.value, 'yyyy-MM-dd')!;
    this.ownerFunction.owner_findByDateCreated(dateCreated);
  }

  owner_findByPetId() {
    this.ownerFunction.owner_findByPetId();
  }

  owner_findByPetName() {
    this.ownerFunction.owner_findByPetName();
  }

  owner_create() {
    this.router.navigate(['/owner/add']);
  }



  navigateBack() {
    this.location.back();
    // this.router.navigate(['/home']);
  }
}
