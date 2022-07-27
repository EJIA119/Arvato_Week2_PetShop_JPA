import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ITopName } from '../pet';
import { PetService } from '../pet.service';

@Component({
  selector: 'app-top-name',
  templateUrl: './top-name.component.html',
  styleUrls: ['./top-name.component.css']
})
export class TopNameComponent implements OnInit {

  constructor(private petService : PetService, private location: Location) { }

  topNameList: ITopName[] = [];
  sub!: Subscription;
  errorMessage: string = '';

  pageTitle = "Pet - Top Name Listing";

  // Doesn't work yet, check the API in spring boot

  ngOnInit(): void {
    this.petService.topName().subscribe({
      next: (topNameList) => this.topNameList = topNameList,
      error: (err) => this.errorMessage = err
    })
  }

  navigateBack(){
    this.location.back();
  }

}
